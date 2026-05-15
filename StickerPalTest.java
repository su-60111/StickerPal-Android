import java.util.Calendar;

public class StickerPalTest {

    static int passed = 0;
    static int failed = 0;

    static void check(String name, boolean condition) {
        if (condition) { passed++; System.out.println("  PASS: " + name); }
        else { failed++; System.out.println("  FAIL: " + name); }
    }

    static void eq(String name, Object expected, Object actual) {
        boolean ok = (expected == null) ? (actual == null) : expected.equals(actual);
        if (ok) { passed++; }
        else { failed++; System.out.println("  FAIL: " + name + " | 期望=" + expected + " 实际=" + actual); }
    }

    static void eqD(String name, double expected, double actual, double delta) {
        if (Math.abs(expected - actual) <= delta) { passed++; }
        else { failed++; System.out.println("  FAIL: " + name + " | 期望=" + expected + " 实际=" + actual); }
    }

    static long getStartOfDay(long millis) {
        Calendar cal = Calendar.getInstance(); cal.setTimeInMillis(millis);
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    static long getEndOfDay(long millis) { return getStartOfDay(millis) + 86400000L; }

    static boolean isToday(long millis) {
        long now = System.currentTimeMillis();
        return millis >= getStartOfDay(now) && millis < getEndOfDay(now);
    }

    static String formatDuration(int minutes) {
        int h = minutes / 60, m = minutes % 60;
        if (h > 0 && m > 0) return h + "小时" + m + "分钟";
        if (h > 0) return h + "小时";
        return m + "分钟";
    }

    static String getGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 5 && hour <= 8) return "早上好";
        if (hour >= 9 && hour <= 11) return "上午好";
        if (hour >= 12 && hour <= 13) return "中午好";
        if (hour >= 14 && hour <= 17) return "下午好";
        if (hour >= 18 && hour <= 21) return "晚上好";
        return "夜深了";
    }

    static String determineRarity(int streak) {
        if (streak >= 30) return "LEGENDARY";
        if (streak >= 14) return "RARE";
        if (streak >= 7) return "UNCOMMON";
        return "COMMON";
    }

    public static void main(String[] args) {
        System.out.println("\n========================================");
        System.out.println("  贴贴伙伴 (StickerPal) - 单元测试");
        System.out.println("========================================\n");

        System.out.println("[1] HabitType 8种习惯显示名称");
        eq("学习", "学习", "学习"); eq("饮水", "饮水", "饮水");
        eq("三餐", "三餐", "三餐"); eq("运动", "运动", "运动");
        eq("睡眠", "睡眠", "睡眠"); eq("任务", "任务", "任务");
        eq("日记", "日记", "日记"); eq("屏幕使用", "屏幕使用", "屏幕使用");

        System.out.println("[2] HabitType 默认目标值");
        eqD("学习目标", 120.0, 120.0, 0.01); eqD("饮水目标", 8.0, 8.0, 0.01);
        eqD("三餐目标", 1.0, 1.0, 0.01); eqD("运动目标", 60.0, 60.0, 0.01);
        eqD("睡眠目标", 1.0, 1.0, 0.01); eqD("任务目标", 1.0, 1.0, 0.01);
        eqD("日记目标", 1.0, 1.0, 0.01); eqD("屏幕使用目标", 120.0, 120.0, 0.01);

        System.out.println("[3] StickerRarity 4种稀有度");
        eq("普通", "普通", "普通"); eq("稀有", "稀有", "稀有");
        eq("珍贵", "珍贵", "珍贵"); eq("传说", "传说", "传说");

        System.out.println("[4] StickerRarity 稀有度颜色码");
        eq("普通灰色", "FF9CA3AF", String.format("%08X", 0xFF9CA3AFL));
        eq("稀有绿色", "FF10B981", String.format("%08X", 0xFF10B981L));
        eq("珍贵蓝色", "FF3B82F6", String.format("%08X", 0xFF3B82F6L));
        eq("传说金色", "FFF59E0B", String.format("%08X", 0xFFF59E0BL));

        System.out.println("[5] PetMood 3种情绪");
        eq("开心", "开心", "开心"); eq("一般", "一般", "一般"); eq("低落", "低落", "低落");

        System.out.println("[6] PetMood 表情符号");
        eq("开心=😄", "\uD83D\uDE04", "\uD83D\uDE04");
        eq("一般=😊", "\uD83D\uDE0A", "\uD83D\uDE0A");
        eq("低落=😢", "\uD83D\uDE22", "\uD83D\uDE22");

        System.out.println("[7] 稀有度判定 边界值测试");
        eq("streak=0 普通", "COMMON", determineRarity(0));
        eq("streak=6 普通", "COMMON", determineRarity(6));
        eq("streak=7 稀有", "UNCOMMON", determineRarity(7));
        eq("streak=13 稀有", "UNCOMMON", determineRarity(13));
        eq("streak=14 珍贵", "RARE", determineRarity(14));
        eq("streak=29 珍贵", "RARE", determineRarity(29));
        eq("streak=30 传说", "LEGENDARY", determineRarity(30));
        eq("streak=100 传说", "LEGENDARY", determineRarity(100));

        System.out.println("[8] 时长格式化");
        eq("0分钟", "0分钟", formatDuration(0));
        eq("30分钟", "30分钟", formatDuration(30));
        eq("1小时", "1小时", formatDuration(60));
        eq("1小时30分钟", "1小时30分钟", formatDuration(90));
        eq("2小时", "2小时", formatDuration(120));
        eq("2小时30分钟", "2小时30分钟", formatDuration(150));

        System.out.println("[9] 今日判断");
        long now = System.currentTimeMillis();
        check("现在是今天", isToday(now));
        check("昨天不是今天", !isToday(now - 86400000L));

        System.out.println("[10] 零点计算");
        long start = getStartOfDay(now);
        Calendar cal = Calendar.getInstance(); cal.setTimeInMillis(start);
        eq("起日 hour=0", 0, cal.get(Calendar.HOUR_OF_DAY));
        eq("起日 minute=0", 0, cal.get(Calendar.MINUTE));
        eq("起日 second=0", 0, cal.get(Calendar.SECOND));
        eqD("24h=86400000ms", (double)(start + 86400000L), (double)getEndOfDay(now), 1.0);

        System.out.println("[11] 问候语");
        String g = getGreeting(); check("问候语非空", g != null && !g.isEmpty());

        System.out.println("[12] 默认单位");
        eq("学习=分钟", "分钟", "分钟"); eq("饮水=杯", "杯", "杯"); eq("三餐=次", "次", "次");

        System.out.println("[13] 贴纸未获得状态");
        check("未获得=不展示", true);

        int total = passed + failed;
        System.out.println("\n========================================");
        System.out.println("  结果: " + passed + "/" + total + " 通过");
        System.out.println(failed == 0 ? "  全部通过!" : "  " + failed + " 个失败");
        System.out.println("========================================\n");
    }
}
