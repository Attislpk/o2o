package com.imooc.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * 对图片进行处理的工具类
 */
public class ImageUtil {
    private static final String basePath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 创建图片缩略图
     * @param thumbnail 图片输入流
     * @param targetAddr 目标地址
     * @return 缩略图的相对路径
     */
    public static String generateThumbnail(File thumbnail, String targetAddr) {
        //缩略图名
        String realFileName = getRandomFileName();
        //缩略图拓展名
        String extension = getFileExtension(thumbnail);
        //缩略图路径
        makeDirPath(targetAddr);
        //缩略图存放的相对路径
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is :" + relativeAddr);
        //文件绝对路径   "E:/JAVACODE/projectdev/image/" + targetAddr + realFileName + extension;
        File absoFile = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("curren complete addr is :" + absoFile.getPath());
        try {
            Thumbnails.of(thumbnail).size(200, 200).watermark(Positions.BOTTOM_RIGHT,
                    ImageIO.read(new File(basePath + "watermark.jpg")), 0.5f).outputQuality(0.8f).
                    toFile(absoFile);
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException("缩略图创建失败" + e.toString());
        }
        return relativeAddr;//返回缩略图的相对路径, 避免不同操作系统绝对路径不一致
    }

    /**
     * 获取输入文件流拓展名
     *
     * @param cFile 输入文件流
     * @return 文件拓展名
     */
    private static String getFileExtension(File cFile) {
        String originalFilename = cFile.getName();
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    /**
     * 获取随机文件名，当前时间+5位随机数
     *
     * @return 随机文件名
     */
    private static String getRandomFileName() {
        String nowTimestr = sDateFormat.format(new Date());
        int rand = random.nextInt(89999) + 10000;
        return nowTimestr + rand;
    }

    /**
     * 创建目标路径所涉及到的文件夹
     *
     * @param targetAddr 目标路径
     *                   例如E:/JAVACODE/projectdev/image/ 则会创建projectdev,image两个文件夹
     */
    private static void makeDirPath(String targetAddr) {
        //绝对路径=根路径+相对路径, 创建目录
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * 将CommonsMultipartFile转换为File类
     * @param cFile  传入的CommonsMultipartFile
     * @return file类
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile){
        File newFile = new File(cFile.getOriginalFilename());
        try {
            cFile.transferTo(newFile);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return newFile;
    }
}
