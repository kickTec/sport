public class TestDemo {
    public static void main(String[] args) {
        StringArrayUtil stringArrayUtil = new StringArrayUtil();
        String ret = stringArrayUtil.contact(new String[]{"AA", "BB", "CC"});
        System.out.println(ret);
    }

    static class StringArrayUtil {
        public String contact(String[] source){
            StringBuilder sb = new StringBuilder();
            if(source != null){
                for(String str:source){
                    sb.append(str);
                    sb.append(",");
                }
            }
            sb.delete(sb.length()-1,sb.length());
            return sb.toString();
        }
    }
}
