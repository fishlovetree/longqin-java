package com.longqin.system.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.mail.internet.MimeUtility; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件工具类，获取列表、上传文件、下载文件、删除文件
 */
public class UploadUtils {
	
	/** 绝对路径 **/
	private static String absolutePath = "." + File.separator + "longqin_system_upload" + File.separator + "files";

	/**
	 * 创建文件夹路径
	 */
	private static void createDirIfNotExists() {
		File upload = new File(absolutePath);
		if (!upload.exists()) {
			upload.mkdirs();
		}
		
//		System.out.println("上传图标所在文件夹："+upload.getAbsolutePath());
	}

	/**
	 * @return 路径下文件
	 */
	public static ArrayList<FileObject> getFileList() {
		// 第一次会创建文件夹
		createDirIfNotExists();

		// 获得指定文件对象
		File file = new File(absolutePath);
		// 获得该文件夹内的所有文件
		File[] array = file.listFiles();
		ArrayList<FileObject> list = new ArrayList<FileObject>();
		for (int i = 0; i < array.length; i++) {
			File temp = array[i];
			if (temp.isFile()) {// 如果是文件
				FileObject fo = new FileObject();
				String size = "";
				long fileS = temp.length();
				// DecimalFormat df = new DecimalFormat("#.00");
				if (fileS < 1024) {
					size = fileS + "BT";
					// size = df.format((double) fileS) + "BT";
				} else if (fileS < 1048576) {
					size = fileS / 1024 + "KB";
					// size = df.format((double) fileS / 1024) + "KB";
				} else if (fileS < 1073741824) {
					size = fileS / 1048576 + "MB";
					// size = df.format((double) fileS / 1048576) + "MB";
				} else {
					size = fileS / 1073741824 + "GB";
					// size = df.format((double) fileS / 1073741824) +"GB";
				}
				fo.setSize(size);
				fo.setName(temp.getName());
				Date date = new Date(temp.lastModified());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				fo.setDate(sdf.format(date));

				list.add(fo);
			}
		}
		return list;
	}

	/**
	 * 上传单个文件，重复文件加（序号）； 最后文件存放路径为：配置在配置文件中
	 * @param inputStream 文件流
	 * @param filename 文件名，如：test.jpg
	 * @return 成功：上传后的文件访问路径，失败返回：null
	 */
	public static String upload(InputStream inputStream, String filename) {
		createDirIfNotExists();

		// 存文件-重复文件加（序号）
		File uploadFile = createOrRenameFile(filename);
		try {
			FileUtils.copyInputStreamToFile(inputStream, uploadFile);

			return uploadFile.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 上传单个文件，重复则覆盖； 最后文件存放路径为：配置在配置文件中
	 * @param inputStream 文件流
	 * @param filename 文件名，如：test.jpg
	 * @return 成功：上传后的文件访问路径，失败返回：null
	 */
	public static String coverUpload(InputStream inputStream, String filename) {
		createDirIfNotExists();

		// 存文件-重复文件覆盖
		File uploadFile = new File(absolutePath, filename);
		try {
			FileUtils.copyInputStreamToFile(inputStream, uploadFile);

			return uploadFile.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
     * @param file     文件
     * @param filename 保存的文件名
     * @return
     */
    @SuppressWarnings("unused")
	private static String coverUpload(MultipartFile file, String filename) {
    	createDirIfNotExists();

		// 文件名不重复，可用这个方法
		File uploadFile = new File(absolutePath, filename);
        try {
            //保存文件
            file.transferTo(uploadFile);
            return uploadFile.getAbsolutePath();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

	/**
	 * 下载文件
	 * 
	 * @param path
	 *            文件名称 如： test.jpg
	 * @return true 下载成功； false 下载失败
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, String filename) {
		createDirIfNotExists();
		File file = new File(absolutePath, filename);
		if (file.exists()) {
			try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;" + getFileName(request, response, filename)); // 这个很重要
			long fileLength = file.length();
			response.setHeader("Content-Length", String.valueOf(fileLength));

			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			OutputStream outputStream = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				outputStream = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					outputStream.write(buffer, 0, i);
					i = bis.read(buffer);
				}
				outputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 下载文件
	 * 
	 * @param path
	 *            文件名称 如： test.jpg
	 * @return true 下载成功； false 下载失败
	 */
	public static void downloadAbsolutePath(HttpServletRequest request, HttpServletResponse response, String path) {
		createDirIfNotExists();
		File file = new File(path);
		if (file.exists()) {
			try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String filename = "";
			String os = System.getProperty("os.name");
		    if(os.toLowerCase().startsWith("win")){// windows系统
		    	filename = path.substring(path.lastIndexOf('\\') + 1);
		    }else{
		    	filename = path.substring(path.lastIndexOf('/') + 1);
		    }
			
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;" + getFileName(request, response, filename)); // 这个很重要
			long fileLength = file.length();
			response.setHeader("Content-Length", String.valueOf(fileLength));

			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			OutputStream outputStream = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				outputStream = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					outputStream.write(buffer, 0, i);
					i = bis.read(buffer);
				}
				outputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件访问的路径upload开始 如： test.jpg
	 * @return true 删除成功； false 删除失败
	 */
	public static boolean delete(String filename) {
		createDirIfNotExists();
		System.out.println(absolutePath);
		File file = new File(absolutePath, filename);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 图片转化成base64字符串，并压缩图片大小
	 * @param filename 文件路径
	 * @return base64字符串
	 */
	@SuppressWarnings("unused")
	private static String encodeBase64File(String filename) {
		int index = filename.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        String type = filename.substring(index + 1).toLowerCase();//转成小写
        
		createDirIfNotExists();
		File file = new File(filename);  
		String result = "";
		if (file.exists()) {
			//FileInputStream fis = null;
			try {
				//将图片转base64
				/*fis = new FileInputStream(file);
				byte[] buffer = new byte[(int)file.length()]; 
				fis.read(buffer);
				fis.close();
				byte[] by = new Base64().encode(buffer); */
				
				//压缩图片
				Image img = ImageIO.read(file); // 构造Image对象
				int width = img.getWidth(null); // 得到源图宽
				int height = img.getHeight(null); // 得到源图长
				
				//System.out.println("压缩前，大小:"+file.length()/1024 + "kb");
				//System.out.println("压缩前，宽:"+ width + "，长:" + height);
				
				int divisor = width / 256;
				int w = width > 256 ? width / divisor : width; 
				int h = width > 256 ? height / divisor : height; 
				// SCALE_SMOOTH的缩略算法生成缩略图片的平滑度的优先级比速度高生成的图片质量比较好但速度慢
				BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				image.getGraphics().drawImage(img, 0, 0, w, h, null);// 绘制缩小后的图
				//输出流
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ImageIO.write(image, type, stream);
				//转为base64
				byte[] by = new Base64().encode(stream.toByteArray());
				result = new String(by); 
		        switch(type){
		        case "jpeg":case"jpg":result = "data:image/jpeg;base64," + result;break;//base64编码的jpeg图片数据
		        case "png":result = "data:image/png;base64," + result;break; //base64编码的png图片数据
		        }
		        
		        stream.close();
				img = null;
				file = null;
				
				//System.out.println("压缩后，大小:"+by.length/1024 + "kb");
				//System.out.println("压缩后，宽:"+ w + "，长:" + h);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return result;
			} catch (IOException e) {
				e.printStackTrace();
				return result;
			}                                                                                                                                                                                                                                     
		}
		return result;
	}
	
	/**
     * 本地文件（图片、excel等）转换成Base64字符串
     *
     * @param imgPath     
     */
    public static String convertFileToBase64(String imgPath) {
    	int index = imgPath.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        String type = imgPath.substring(index + 1).toLowerCase();//转成小写
        
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
//            System.out.println("文件大小（字节）="+in.available());
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            System.out.println(""+e.getMessage());
            return "";
        }
        // 对字节数组进行Base64编码，得到Base64编码的字符串
        if(null != data && data.length > 0){
	        byte[] by = new Base64().encode(data);
	        String result = new String(by);
			
	        switch(type){
	        case "doc":result = "data:application/msword;base64," + result;break;
	        case "docx":result = "data:application/vnd.openxmlformats-officedocument.wordprocessingml.document;base64," + result;break;
	        case "xls":result = "data:application/vnd.ms-excel;base64," + result;break;
	        case "xlsx":result = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64," + result;break;
	        case "pdf":result = "data:application/pdf;base64," + result;break;
	        case "ppt":result = "data:application/vnd.ms-powerpoint;base64," + result;break;
	        case "pptx":result = "data:application/vnd.openxmlformats-officedocument.presentationml.presentation;base64," + result;break;
	        case "txt":result = "data:text/plain;base64," + result;break;
	        case "png":result = "data:image/png;base64," + result;break;
	        case "jpeg":case "jpg":result = "data:image/jpeg;base64," + result;break;
	        case "gif":result = "data:image/gif;base64," + result;break;
	        case "svg":result = "data:image/svg+xml;base64," + result;break;
	        case "ico":result = "data:image/x-icon;base64," + result;break;
	        case "bmp":result = "data:image/bmp;base64," + result;break;
	        }
			
			return result;
        }
        return "";
    }
    
    /**
     * 本地文件（图片、excel等）转换成Base64字符串
     *
     * @param imgPath     
     */
    public static byte[] convertFileToBlob(String imgPath) {
    	createDirIfNotExists();
		File file = new File(imgPath);  
		if (file.exists()) {
			byte[] b;
			try {
				b = FileCopyUtils.copyToByteArray(file);
				return b;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
    }

	public static String getBaseLogoBase64() {
        byte[] data = null;
        // 读取图片字节数组
        try {
        	//获得文件流。 ClassPathResource自动定位到resources目录
        	String imgPath = "";
        	String os = System.getProperty("os.name");
    	    if(os.toLowerCase().startsWith("win")){// windows系统
    	    	imgPath = "templates\\OrgLogo\\wellsun_logo.png";
    	    }else{
    	    	imgPath = "templates/OrgLogo/wellsun_logo.png";
    	    }
            ClassPathResource cpr = new ClassPathResource(imgPath);
            InputStream in = cpr.getInputStream();
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组进行Base64编码，得到Base64编码的字符串
        if(null != data && data.length > 0){
	        byte[] by = new Base64().encode(data);
	        String result = "data:image/png;base64," +  new String(by);//base64编码的png图片数据
			return result;
        }
        return "";
	}
    
    public String doCompressFile(String imgPath, int size){
    	
        return "";
    }

	/**
     * 字节转kb/mb/gb
     * @param size
     * @return
     */
    @SuppressWarnings("unused")
	private static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

	/**
	 *
	 * @param from
	 *            fileInfo[0]: toPrefix; fileInfo[1]:toSuffix;
	 * @return
	 */
	private static String[] getFileInfo(String fileName) {
		int index = fileName.lastIndexOf(".");
		String toPrefix = "";
		String toSuffix = "";
		if (index == -1) {
			toPrefix = fileName;
		} else {
			toPrefix = fileName.substring(0, index);
			toSuffix = fileName.substring(index, fileName.length());
		}
		return new String[] { toPrefix, toSuffix };
	}

	/**
	 * sdcard/pic/tanyang.jpg
	 *
	 * toPrefix: tanyang toSuffix: tanyang.jpg
	 * 
	 * @param from
	 * @param toPrefix
	 * @param toSuffix
	 * @return
	 */
	private static File createOrRenameFile(String filename) {
		String[] fileInfo = getFileInfo(filename);
		String toPrefix = fileInfo[0];
		String toSuffix = fileInfo[1];

		File newFile = new File(absolutePath, filename);
		for (int i = 1; newFile.exists() && i < Integer.MAX_VALUE; i++) {
			newFile = new File(absolutePath, toPrefix + '(' + i + ')' + toSuffix);
		}
		return newFile;
	}

	/**
	 * @Description 下载文档，文件编码格式
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws Exception
	 * @Time 2019年7月24日
	 * @Author dj
	 */
	private static String getFileName(HttpServletRequest request, HttpServletResponse response, String fileName) {
		String userAgent = request.getHeader("User-Agent");
		String newFileName = null;
		try {
			fileName = URLEncoder.encode(fileName, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();
			// IE浏览器，只能采用URLEncoder编码
			if (userAgent.indexOf("msie") != -1) {
				newFileName = "filename=\"" + fileName + "\"";
			}
			// Opera浏览器只能采用filename*
			else if (userAgent.indexOf("opera") != -1) {
				newFileName = "filename*=UTF-8''" + fileName;
			}
			// Safari浏览器，只能采用ISO编码的中文输出
			else if (userAgent.indexOf("safari") != -1) {
				try {
					newFileName = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			// Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
			else if (userAgent.indexOf("applewebkit") != -1) {
				try {
					fileName = MimeUtility.encodeText(fileName, "UTF8", "B");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				newFileName = "filename=\"" + fileName + "\"";
			}
			// FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
			else if (userAgent.indexOf("mozilla") != -1) {
				newFileName = "filename*=UTF-8''" + fileName;
			}
		}
		// 文件名编码结束。

		return newFileName;
	}
}