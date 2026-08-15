#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Regenerate SeedData.kt from the per-type word datasets (scripts/hint_data/).

Word TYPES (structural, not topic):
  - NORMAL : a single standalone token (no space)        -> 80 words
  - PRO    : a natural TWO-part compound (exactly one space) -> 80 words

Dataset rules enforced here (mirrored by SeedDataTest):
  - exactly 3 hints per word (HINTS_PER_WORD)
  - hints unique within a word, non-blank, single token (no ASCII space;
    ZWNJ نیم‌فاصله IS allowed in words AND hints — correct Persian orthography)
  - a hint never contains its word and its word never contains the hint
  - a hint is never a substring of ANY bank word, and no bank word is ever a
    substring of a hint (a hint must not leak another word's identity)
  - no hint equals another word of the bank, directly or as a token
  - words unique across the whole bank; 80 words per type, order preserved
  - each type is balanced across the 4 semantic domains
    (اشیا / حیوانات / غذا / شغل) with 20 words per domain
  - no hint directly reveals EITHER part of a PRO two-part compound
  - no hint reused more than MAX_HINT_REUSE times across the bank

Corruption guard: on the first fully-passing run the generator pins a sha256
fingerprint of the data files to scripts/bank_fingerprint.json; later runs fail
loudly if the files have drifted.
"""
import hashlib
import io
import json
import os
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
DATA = os.path.join(HERE, "hint_data")
OUT = os.path.join(HERE, "..", "app", "src", "main", "java", "ir", "mehdi",
                   "imposter", "data", "local", "SeedData.kt")
FINGERPRINT_FILE = os.path.join(HERE, "bank_fingerprint.json")
ZWNJ = "\u200c"

TYPES = [("NORMAL", "normal.py", "عادی"),
         ("PRO", "pro.py", "حرفه‌ای")]

HINTS_PER_WORD = 3
WORDS_PER_TYPE = 80
MAX_HINT_REUSE = 3
DOMAINS = ["اشیا", "حیوانات", "غذا", "شغل"]
WORDS_PER_DOMAIN = 20

# For PRO compounds, a hint must not directly reveal either part.
PRO_PART_LEAKS = False  # checked below via word-part tokens


def load_words(wtype, filename):
    path = os.path.join(DATA, filename)
    ns = {}
    with io.open(path, encoding="utf-8") as fh:
        code = fh.read()
    exec(compile(code, path, "exec"), ns)
    words = ns["WORDS"]
    return [(wtype, w.strip(), dom, [h.strip() for h in hints])
            for w, dom, hints in words]


def check(cond, msg, errors):
    if not cond:
        errors.append(msg)


def main():
    all_entries = []
    errors = []

    type_token_counts = {"NORMAL": 1, "PRO": 2}

    for wtype, filename, _ in TYPES:
        entries = load_words(wtype, filename)
        expected_tokens = type_token_counts[wtype]
        if len(entries) != WORDS_PER_TYPE:
            errors.append("%s: expected %d words, found %d"
                          % (wtype, WORDS_PER_TYPE, len(entries)))
        for _, word, domain, hints in entries:
            # ZWNJ (نیم‌فاصله) IS allowed — it is the correct Persian
            # orthography for compounds (چراغ‌قوه) and is not a space, so it
            # does not affect token counts. Only ASCII spaces structure the
            # bank (NORMAL=0, PRO=1).
            if word.count(" ") != expected_tokens - 1:
                errors.append("%s '%s': expected %d part(s), got '%s'"
                              % (wtype, word, expected_tokens, word))
            if domain not in DOMAINS:
                errors.append("%s '%s': unknown domain '%s'"
                              % (wtype, word, domain))
            if len(hints) != HINTS_PER_WORD:
                errors.append("'%s': must have exactly %d hints, found %d"
                              % (word, HINTS_PER_WORD, len(hints)))
            if len(set(hints)) != len(hints):
                errors.append("'%s': hints must be unique, got %s"
                              % (word, hints))
            for h in hints:
                if not h:
                    errors.append("'%s': blank hint" % word)
                elif " " in h:
                    errors.append("'%s': hint '%s' must be a single token"
                                  % (word, h))
            # PRO: a hint must not be (or contain) either part of the compound.
            if wtype == "PRO":
                parts = [p for p in word.split(" ") if p]
                for h in hints:
                    for part in parts:
                        if h == part or h in part or part in h:
                            errors.append(
                                "'%s': hint '%s' leaks part '%s' of the compound"
                                % (word, h, part))
        all_entries.extend(entries)

    # ── domain balance per type ───────────────────────────────────────
    for wtype, _, _ in TYPES:
        type_entries = [e for e in all_entries if e[0] == wtype]
        for dom in DOMAINS:
            n = sum(1 for e in type_entries if e[2] == dom)
            if n != WORDS_PER_DOMAIN:
                errors.append("%s: domain '%s' must have %d words, found %d"
                              % (wtype, dom, WORDS_PER_DOMAIN, n))

    # ── bank-wide textual checks ───────────────────────────────────────
    all_words = [w for _, w, _, _ in all_entries]
    if len(set(all_words)) != len(all_words):
        dupes = {w for w in all_words if all_words.count(w) > 1}
        errors.append("duplicate words: %s" % sorted(dupes))

    hint_usage = {}
    for _, word, _, hints in all_entries:
        for h in hints:
            if h in all_words:
                errors.append("'%s': hint '%s' is itself another bank word"
                              % (word, h))
            for ow in all_words:
                if h != "" and (h in ow):
                    errors.append("'%s': hint '%s' is inside bank word '%s'"
                                  % (word, h, ow))
                if ow in h:
                    errors.append("'%s': hint '%s' contains bank word '%s'"
                                  % (word, h, ow))
            hint_usage[h] = hint_usage.get(h, 0) + 1

    for _, word, _, hints in all_entries:
        for h in hints:
            if h in word:
                errors.append("'%s': hint '%s' is part of its own word"
                              % (word, h))
            if word in h:
                errors.append("'%s': word is contained inside hint '%s'"
                              % (word, h))

    for h, n in sorted(hint_usage.items(), key=lambda kv: -kv[1]):
        if n > MAX_HINT_REUSE:
            errors.append("hint '%s' reused %d times (max %d)"
                          % (h, n, MAX_HINT_REUSE))

    # ── fingerprint (corruption guard) ────────────────────────────────
    data_hash = hashlib.sha256()
    for _, filename, _ in TYPES:
        with io.open(os.path.join(DATA, filename), "rb") as fh:
            data_hash.update(fh.read())
    digest = data_hash.hexdigest()

    if errors:
        print("Bank validation FAILED (%d errors):" % len(errors))
        for e in errors:
            print("  - %s" % e)
        sys.exit(1)

    if os.path.exists(FINGERPRINT_FILE):
        with io.open(FINGERPRINT_FILE, encoding="utf-8") as fh:
            pinned = json.load(fh)
        if pinned.get("sha256") != digest:
            print("Bank FINGERPRINT MISMATCH - the data files were edited "
                  "outside this generator.")
            print("  pinned: %s" % pinned.get("sha256"))
            print("  actual: %s" % digest)
            sys.exit(1)
    else:
        with io.open(FINGERPRINT_FILE, "w", encoding="utf-8") as fh:
            json.dump({"sha256": digest}, fh, indent=2)
            fh.write("\n")
        print("Fingerprint file created: %s" % FINGERPRINT_FILE)

    print("Bank OK: %d words, top hint reuse: %s"
          % (len(all_entries), max(hint_usage.values())))

    # ── emit SeedData.kt ───────────────────────────────────────────────
    lines = []
    lines.append("package ir.mehdi.imposter.data.local\n")
    lines.append("import ir.mehdi.imposter.data.local.entity.WordEntity\n")
    lines.append("/**")
    lines.append(" * The full word dataset for the game, grouped by word TYPE:")
    lines.append(" *")
    lines.append(" * - NORMAL : one standalone word (e.g. «کتاب»)")
    lines.append(" * - PRO    : a natural two-part compound (e.g. «ماشین لباسشویی»)")
    lines.append(" *")
    lines.append(" * The four semantic domains (اشیا / حیوانات / غذا / شغل) are NOT")
    lines.append(" * exposed in the UI; they only keep the dataset balanced and varied.")
    lines.append(" *")
    lines.append(" * Dataset rules (enforced by SeedDataTest and scripts/hints_gen.py):")
    lines.append(" * - Every word has EXACTLY [HINTS_PER_WORD] hints.")
    lines.append(" * - Every hint is exactly ONE word.")
    lines.append(" * - Hints never contain the word itself (even as a substring),")
    lines.append(" *   never contain any OTHER bank word, and are never synonyms/")
    lines.append(" *   direct features of the word.")
    lines.append(" * - The three hints of a word cover different angles instead of")
    lines.append(" *   repeating one idea.")
    lines.append(" * - For PRO compounds, no hint reveals either part directly.")
    lines.append(" *")
    lines.append(" * Source of truth: the per-type data files under scripts/hint_data/ -")
    lines.append(" * regenerate with scripts/hints_gen.py. Adding words: extend the data")
    lines.append(" * files and re-run the generator; ids are assigned from list order.")
    lines.append(" */")
    lines.append("object SeedData {\n")
    lines.append("    const val HINTS_PER_WORD = %d" % HINTS_PER_WORD)
    lines.append("")
    lines.append("    private data class SeedWord(")
    lines.append("        val type: String,")
    lines.append("        val word: String,")
    lines.append("        val hints: List<String>")
    lines.append("    )")
    lines.append("")
    lines.append("    fun getAllWords(): List<WordEntity> =")
    lines.append("        allWords().mapIndexed { index, seed ->")
    lines.append("            WordEntity(")
    lines.append("                id = (index + 1).toLong(),")
    lines.append("                type = seed.type,")
    lines.append("                word = seed.word,")
    lines.append("                hints = seed.hints.joinToString(\"|||\")")
    lines.append("            )")
    lines.append("        }")
    lines.append("")
    lines.append("    private fun allWords(): List<SeedWord> = buildList {")
    for wtype, filename, _ in TYPES:
        lines.append("        %s().forEach { add(it) }" % wtype.lower())
    lines.append("    }")
    lines.append("")

    for wtype, filename, persian in TYPES:
        entries = load_words(wtype, filename)
        lines.append("    // ─────────────────────────────────────────────────")
        lines.append("    // %s «%s»" % (wtype, persian))
        lines.append("    // ─────────────────────────────────────────────────")
        lines.append("")
        lines.append("    private fun %s(): List<SeedWord> = listOf(" % wtype.lower())
        for _, word, _, hints in entries:
            hints_repr = ", ".join('"%s"' % h for h in hints)
            lines.append('        SeedWord("%s", "%s", listOf(%s)),'
                         % (wtype, word, hints_repr))
        lines.append("    )")
        lines.append("")

    lines.append("}")
    lines.append("")

    with io.open(OUT, "w", encoding="utf-8") as fh:
        fh.write("\n".join(lines))
    print("Wrote %s" % OUT)


if __name__ == "__main__":
    main()
