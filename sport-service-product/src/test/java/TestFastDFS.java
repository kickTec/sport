import com.kenick.sport.product.utils.FastDFSUtil;

public class TestFastDFS {
    public static void main(String[] args) {
        String url = "http://192.168.200.140:81/group1/M00/00/00/wKjIjFx_I9CANZYVAAAatgSQzuw207.png";
        String fileIdFromUrl = FastDFSUtil.getFileIdFromUrl(url);
        System.out.println(fileIdFromUrl);
    }
}
