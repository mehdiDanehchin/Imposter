package ir.mehdi.imposter.data.local

import ir.mehdi.imposter.data.local.entity.WordEntity

/**
 * The full word dataset for the game, grouped by word TYPE:
 *
 * - NORMAL : one standalone word (e.g. «کتاب»)
 * - PRO    : a natural two-part compound (e.g. «ماشین لباسشویی»)
 *
 * The four semantic domains (اشیا / حیوانات / غذا / شغل) are NOT
 * exposed in the UI; they only keep the dataset balanced and varied.
 *
 * Dataset rules (enforced by SeedDataTest and scripts/hints_gen.py):
 * - Every word has EXACTLY [HINTS_PER_WORD] hints.
 * - Every hint is exactly ONE word.
 * - Hints never contain the word itself (even as a substring),
 *   never contain any OTHER bank word, and are never synonyms/
 *   direct features of the word.
 * - The three hints of a word cover different angles instead of
 *   repeating one idea.
 * - For PRO compounds, no hint reveals either part directly.
 *
 * Source of truth: the per-type data files under scripts/hint_data/ -
 * regenerate with scripts/hints_gen.py. Adding words: extend the data
 * files and re-run the generator; ids are assigned from list order.
 */
object SeedData {

    const val HINTS_PER_WORD = 3

    private data class SeedWord(
        val type: String,
        val word: String,
        val hints: List<String>
    )

    fun getAllWords(): List<WordEntity> =
        allWords().mapIndexed { index, seed ->
            WordEntity(
                id = (index + 1).toLong(),
                type = seed.type,
                word = seed.word,
                hints = seed.hints.joinToString("|||")
            )
        }

    private fun allWords(): List<SeedWord> = buildList {
        normal().forEach { add(it) }
        pro().forEach { add(it) }
    }

    // ─────────────────────────────────────────────────
    // NORMAL «عادی»
    // ─────────────────────────────────────────────────

    private fun normal(): List<SeedWord> = listOf(
        SeedWord("NORMAL", "اختاپوس", listOf("هشت", "جوهر", "مکنده")),
        SeedWord("NORMAL", "سفره‌ماهی", listOf("پهن", "کف", "استتار")),
        SeedWord("NORMAL", "دارکوب", listOf("منقار", "تنه", "ریتم")),
        SeedWord("NORMAL", "خفاش", listOf("پژواک", "خون", "وارونه")),
        SeedWord("NORMAL", "شترمرغ", listOf("گردن", "دفن", "تخم")),
        SeedWord("NORMAL", "سنجاب", listOf("فندق", "دم", "ذخیره")),
        SeedWord("NORMAL", "کرم", listOf("ابریشم", "پروانه", "باران")),
        SeedWord("NORMAL", "کرگدن", listOf("شاخ", "گل", "کلفت")),
        SeedWord("NORMAL", "پنگوئن", listOf("کت", "لغزش", "یخبندان")),
        SeedWord("NORMAL", "لاک‌پشت", listOf("خانه", "خرگوش", "صدسالگی")),
        SeedWord("NORMAL", "زالو", listOf("درمان", "مرداب", "چسب")),
        SeedWord("NORMAL", "جغد", listOf("حکمت", "چرخش", "خاموش")),
        SeedWord("NORMAL", "آرمادیلو", listOf("زره", "گلوله", "حفر")),
        SeedWord("NORMAL", "آفتاب‌پرست", listOf("رنگ", "چشم", "زبان")),
        SeedWord("NORMAL", "دلفین", listOf("هوش", "پرش", "سوت")),
        SeedWord("NORMAL", "کوسه", listOf("ترس", "آرواره", "مثلث")),
        SeedWord("NORMAL", "گرگ", listOf("زوزه", "مادر‌بزرگ", "گله")),
        SeedWord("NORMAL", "قورباغه", listOf("جهش", "برکه", "شاهزاده")),
        SeedWord("NORMAL", "طاووس", listOf("بادبزن", "غرور", "رقص")),
        SeedWord("NORMAL", "کوالا", listOf("اکالیپتوس", "استرالیا", "تنبل")),
        SeedWord("NORMAL", "غواص", listOf("ماسک", "عمق", "مروارید")),
        SeedWord("NORMAL", "باستان‌شناس", listOf("گنج", "موزه", "کاوش")),
        SeedWord("NORMAL", "دوبلور", listOf("کارتون", "میکروفون", "استودیو")),
        SeedWord("NORMAL", "آتش‌نشان", listOf("نردبان", "دود", "آژیر")),
        SeedWord("NORMAL", "زرگر", listOf("جواهر", "عروس", "کوره")),
        SeedWord("NORMAL", "مجسمه‌ساز", listOf("مرمر", "تراش", "نمایشگاه")),
        SeedWord("NORMAL", "قالیباف", listOf("گره", "نقش", "پود")),
        SeedWord("NORMAL", "عکاس", listOf("لحظه", "فلاش", "ظهور")),
        SeedWord("NORMAL", "دلقک", listOf("بینی", "خنده", "غم")),
        SeedWord("NORMAL", "مرمت‌کار", listOf("تابلو", "آسیب", "کهنه")),
        SeedWord("NORMAL", "معدن‌چی", listOf("زغال", "تونل", "کلنگ")),
        SeedWord("NORMAL", "خلبان", listOf("کابین", "فرود", "کاپیتان")),
        SeedWord("NORMAL", "دندان‌پزشک", listOf("مته", "مطب", "درد")),
        SeedWord("NORMAL", "کارآگاه", listOf("معما", "ذره‌بین", "شاهد")),
        SeedWord("NORMAL", "هواشناس", listOf("پیش‌بینی", "طوفان", "ماهواره")),
        SeedWord("NORMAL", "گزارشگر", listOf("استادیوم", "فوتبال", "زنده")),
        SeedWord("NORMAL", "داروساز", listOf("نسخه", "بیمار", "پیشخوان")),
        SeedWord("NORMAL", "دریانورد", listOf("کشتی", "بادبان", "افق")),
        SeedWord("NORMAL", "نجاتگر", listOf("استخر", "برج", "غریق")),
        SeedWord("NORMAL", "ساعت‌ساز", listOf("چرخ‌دنده", "مچ", "کوک")),
        SeedWord("NORMAL", "مترونوم", listOf("ضرب", "تمرین", "هرم")),
        SeedWord("NORMAL", "شطرنج", listOf("مهره", "فکر", "وزیر")),
        SeedWord("NORMAL", "قطب‌نما", listOf("جهت", "گم", "آهنربا")),
        SeedWord("NORMAL", "چراغ‌قوه", listOf("تاریکی", "باتری", "پرتو")),
        SeedWord("NORMAL", "کرونومتر", listOf("زمان", "دور", "رکورد")),
        SeedWord("NORMAL", "خیاطی", listOf("سوزن", "پارچه", "پدال")),
        SeedWord("NORMAL", "گرامافون", listOf("شیپور", "صفحه", "هندل")),
        SeedWord("NORMAL", "بادبادک", listOf("ریسمان", "بهار", "اوج")),
        SeedWord("NORMAL", "تلسکوپ", listOf("کهکشان", "اسکوپ", "جیمز")),
        SeedWord("NORMAL", "تخته‌سیاه", listOf("گچ", "کلاس", "پاک‌کن")),
        SeedWord("NORMAL", "چتر", listOf("تاشو", "دسته", "خیس")),
        SeedWord("NORMAL", "ترازو", listOf("سنگینی", "بازار", "عدالت")),
        SeedWord("NORMAL", "قهوه‌ساز", listOf("عطر", "فنجان", "فیلتر")),
        SeedWord("NORMAL", "میکروسکوپ", listOf("باکتری", "عدسی", "ذره")),
        SeedWord("NORMAL", "پاندول", listOf("آویزان", "نوسان", "ثانیه")),
        SeedWord("NORMAL", "اکسیژن", listOf("تنفس", "قله", "گیاه")),
        SeedWord("NORMAL", "آینه", listOf("چهره", "بازتاب", "بغل")),
        SeedWord("NORMAL", "فندک", listOf("جرقه", "سیگار", "شمع")),
        SeedWord("NORMAL", "دفترچه", listOf("خط‌دار", "خاطره", "جیبی")),
        SeedWord("NORMAL", "گاوصندوق", listOf("دزد", "ارز", "کلید")),
        SeedWord("NORMAL", "قورمه‌سبزی", listOf("لیمو", "جمعه", "خورش")),
        SeedWord("NORMAL", "فسنجان", listOf("گردو", "انار", "اردک")),
        SeedWord("NORMAL", "ته‌دیگ", listOf("برشته", "زیر", "جایزه")),
        SeedWord("NORMAL", "حلیم", listOf("کش‌دار", "دارچین", "گندم")),
        SeedWord("NORMAL", "فالوده", listOf("یخ", "تابستان", "رشته")),
        SeedWord("NORMAL", "سمنو", listOf("جوانه", "نوروز", "سبزه")),
        SeedWord("NORMAL", "کشک", listOf("نعناع", "ترش", "دوغ")),
        SeedWord("NORMAL", "پشمک", listOf("پنبه", "قنادی", "سبک")),
        SeedWord("NORMAL", "اسپرسو", listOf("تلخ", "کف", "ایتالیا")),
        SeedWord("NORMAL", "پاستا", listOf("سس", "ایتالیایی", "چنگال")),
        SeedWord("NORMAL", "شله‌زرد", listOf("نذر", "زعفران", "برنج")),
        SeedWord("NORMAL", "باسلوق", listOf("ژله‌ای", "اصفهان", "سوغاتی")),
        SeedWord("NORMAL", "کباب", listOf("سیخ", "منقل", "زغال")),
        SeedWord("NORMAL", "کمپوت", listOf("قوطی", "میوه", "شهد")),
        SeedWord("NORMAL", "میرزاقاسمی", listOf("دودی", "گیلان", "بادمجان")),
        SeedWord("NORMAL", "دلمه", listOf("برگ", "انگور", "پیچیدن")),
        SeedWord("NORMAL", "سوپ", listOf("بیمار", "کاسه", "پیش‌غذا")),
        SeedWord("NORMAL", "زولبیا", listOf("رمضان", "افطار", "حلقه")),
        SeedWord("NORMAL", "کله‌پاچه", listOf("سحر", "چرب", "خماری")),
        SeedWord("NORMAL", "گز", listOf("پسته", "چسب", "نبات")),
    )

    // ─────────────────────────────────────────────────
    // PRO «حرفه‌ای»
    // ─────────────────────────────────────────────────

    private fun pro(): List<SeedWord> = listOf(
        SeedWord("PRO", "ماشین لباسشویی", listOf("صابون", "حوله", "تمیز")),
        SeedWord("PRO", "عینک آفتابی", listOf("ساحل", "تابش", "خیره")),
        SeedWord("PRO", "ساعت دیواری", listOf("عقربه", "تیک‌تاک", "آویز")),
        SeedWord("PRO", "تلفن همراه", listOf("جیب", "شارژ", "پیامک")),
        SeedWord("PRO", "کیف پول", listOf("چرم", "کارت", "سکه")),
        SeedWord("PRO", "میز تحریر", listOf("کشو", "نوشتن", "قلم")),
        SeedWord("PRO", "کمد لباس", listOf("چوبی", "رخت", "مرتب")),
        SeedWord("PRO", "فرش دستباف", listOf("گره", "طرح", "قیمت")),
        SeedWord("PRO", "پنجره دوجداره", listOf("عایق", "شیشه", "سرد")),
        SeedWord("PRO", "دوچرخه برقی", listOf("باتری", "پدال", "تپه")),
        SeedWord("PRO", "چتر نجات", listOf("ارتفاع", "بازشدن", "سقوط")),
        SeedWord("PRO", "قفل دیجیتال", listOf("کد", "اثر‌انگشت", "ورودی")),
        SeedWord("PRO", "چوب لباسی", listOf("آویز", "شانه", "میخ")),
        SeedWord("PRO", "کولر گازی", listOf("خنک", "دما", "گرما")),
        SeedWord("PRO", "جارو برقی", listOf("مکش", "سیم", "صدا")),
        SeedWord("PRO", "اتو بخار", listOf("چین", "پارچه", "داغ")),
        SeedWord("PRO", "کیسه خواب", listOf("چادر", "اردو", "گرم")),
        SeedWord("PRO", "سطل زباله", listOf("آشغال", "پلاستیک", "کوچه")),
        SeedWord("PRO", "چراغ مطالعه", listOf("کتاب", "درس", "گردن")),
        SeedWord("PRO", "صندلی راحتی", listOf("تکیه", "نرم", "استراحت")),
        SeedWord("PRO", "سگ شکاری", listOf("بویایی", "تعقیب", "قلاده")),
        SeedWord("PRO", "گربه وحشی", listOf("چنگال", "تنها", "خانگی")),
        SeedWord("PRO", "اسب مسابقه", listOf("یال", "میدان", "سرعت")),
        SeedWord("PRO", "گاو شیری", listOf("طویله", "دوشیدن", "لبنیات")),
        SeedWord("PRO", "ماهی قرمز", listOf("حوض", "عید", "تزئینی")),
        SeedWord("PRO", "پرنده مهاجر", listOf("کوچ", "دسته", "پاییز")),
        SeedWord("PRO", "خرس قطبی", listOf("بزرگ", "شناگر", "برف")),
        SeedWord("PRO", "شیر کوهی", listOf("صخره", "قله", "غرش")),
        SeedWord("PRO", "گرگ خاکستری", listOf("گله", "گرسنگی", "شبگرد")),
        SeedWord("PRO", "فیل آفریقایی", listOf("ساوانا", "گوش", "دشت")),
        SeedWord("PRO", "زنبور عسل", listOf("کندو", "نیش", "شهد")),
        SeedWord("PRO", "نهنگ آبی", listOf("آب‌فشان", "اقیانوس", "غول")),
        SeedWord("PRO", "پنگوئن امپراتور", listOf("سرما", "قد", "شکم")),
        SeedWord("PRO", "خرچنگ دریایی", listOf("کنار", "ساحل", "پختن")),
        SeedWord("PRO", "مار زنگی", listOf("جغجغه", "زهر", "بیابان")),
        SeedWord("PRO", "اسب دریایی", listOf("عجیب", "ظریف", "پوزه")),
        SeedWord("PRO", "عقاب طلایی", listOf("بینایی", "شکارچی", "بلندی")),
        SeedWord("PRO", "گوزن شمالی", listOf("سورتمه", "بابا‌نوئل", "مهاجرت")),
        SeedWord("PRO", "پشه مالاریا", listOf("لرز", "گرمسیری", "بیماری")),
        SeedWord("PRO", "طوطی سخنگو", listOf("تقلید", "قفس", "صحبت")),
        SeedWord("PRO", "چای سبز", listOf("فنجان", "سلامتی", "چینی")),
        SeedWord("PRO", "ذرت مکزیکی", listOf("کره", "فلفل", "سس")),
        SeedWord("PRO", "باقالی پلو", listOf("شوید", "مخصوص", "غذا")),
        SeedWord("PRO", "شیرینی نخودچی", listOf("مهمانی", "آرد", "قند")),
        SeedWord("PRO", "نان سنگک", listOf("تنور", "خمیر", "کنجد")),
        SeedWord("PRO", "نان بربری", listOf("کشیده", "داغ", "تازه")),
        SeedWord("PRO", "نان باگت", listOf("فرانسوی", "پوسته", "طول")),
        SeedWord("PRO", "پنیر محلی", listOf("روستا", "مزه", "شور")),
        SeedWord("PRO", "ماست چکیده", listOf("سفت", "صافی", "ظرف")),
        SeedWord("PRO", "قهوه ترک", listOf("جوش", "تفاله", "هل")),
        SeedWord("PRO", "سالاد فصل", listOf("خیار", "کاهو", "سبزیجات")),
        SeedWord("PRO", "بستنی سنتی", listOf("قیف", "خامه", "سرد")),
        SeedWord("PRO", "کیک شکلاتی", listOf("تولد", "خمیر", "دسر")),
        SeedWord("PRO", "شربت آلبالو", listOf("پیاله", "ترش", "یخ")),
        SeedWord("PRO", "آش جو", listOf("رقیق", "مقوی", "هضم")),
        SeedWord("PRO", "حلوا ارده", listOf("شیره", "عصرانه", "چاق")),
        SeedWord("PRO", "کیک یزدی", listOf("قالب", "خانگی", "وانیل")),
        SeedWord("PRO", "شیر کاکائو", listOf("کودک", "مدرسه", "نوشیدنی")),
        SeedWord("PRO", "نوشابه گازدار", listOf("مهمانی", "بطری", "نوشیدن")),
        SeedWord("PRO", "آب معدنی", listOf("چشمه", "تشنه", "بطری")),
        SeedWord("PRO", "راننده تاکسی", listOf("کرایه", "فرمان", "مسافر")),
        SeedWord("PRO", "مهندس عمران", listOf("جاده", "نقشه", "بتن")),
        SeedWord("PRO", "پزشک اطفال", listOf("نوزاد", "واکسن", "کودک")),
        SeedWord("PRO", "معلم ریاضی", listOf("فرمول", "عدد", "محاسبه")),
        SeedWord("PRO", "پلیس راهنمایی", listOf("چهارراه", "جریمه", "یونیفورم")),
        SeedWord("PRO", "وکیل مدافع", listOf("دادگاه", "دفاع", "پرونده")),
        SeedWord("PRO", "خلبان جنگنده", listOf("رادار", "مانور", "نبرد")),
        SeedWord("PRO", "نقاش ساختمان", listOf("غلتک", "قلم‌مو", "دکور")),
        SeedWord("PRO", "بازیگر سینما", listOf("صحنه", "دیالوگ", "جایزه")),
        SeedWord("PRO", "معلم خصوصی", listOf("تدریس", "شاگرد", "هزینه")),
        SeedWord("PRO", "پرستار بچه", listOf("گهواره", "نگهداری", "آرام")),
        SeedWord("PRO", "مشاور املاک", listOf("قرارداد", "اجاره", "سند")),
        SeedWord("PRO", "راننده کامیون", listOf("جاده", "بار", "سنگین")),
        SeedWord("PRO", "مهماندار هواپیما", listOf("کمربند", "لبخند", "خدمات")),
        SeedWord("PRO", "استاد دانشگاه", listOf("دانشجو", "پژوهش", "کتاب")),
        SeedWord("PRO", "آشپز رستوران", listOf("قابلمه", "اجاق", "سفارش")),
        SeedWord("PRO", "مربی شنا", listOf("آموزش", "کرال", "سلامتی")),
        SeedWord("PRO", "نگهبان بانک", listOf("اسلحه", "امنیت", "درب")),
        SeedWord("PRO", "مجری تلویزیون", listOf("استودیو", "زنده", "مصاحبه")),
        SeedWord("PRO", "عکاس مراسم", listOf("عروسی", "لبخند", "سالن")),
    )

}
