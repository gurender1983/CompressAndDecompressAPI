package com.compress.compressapi.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

@Service
public class CompressService  {
	
	 static int i;	
	 List<String> filesListInDir = new ArrayList<String>();
	
	public void zipFolderFile(String inputPath, String compressedPath, String fileSize )
	{
		try
		{
			int fileSize1=Integer.parseInt(fileSize);
			File dir=new File(inputPath);
			//FileInputStream fin = new FileInputStream(dir);
			FileOutputStream fos = new FileOutputStream(compressedPath);
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			populateFilesList(dir);
			
			for(String filePath :  filesListInDir)
			{
				ByteBuffer bbuf = ByteBuffer.allocate(fileSize1);
				bbuf.limit(fileSize1);
				System.out.println("Zipping : "+filePath);
				ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
				zos.putNextEntry(ze);
				FileInputStream fin = new FileInputStream(filePath);
				byte[] buffer = bbuf.array();
				int len;
				while((len=fin.read(buffer))>0)
				{
					zos.write(buffer, 0, len);
				}
				zos.closeEntry();
				fin.close();
			}
			zos.close();
			fos.close();
			
		}catch(IOException ex)
		{
			System.err.println("I/O error: " + ex);
		}
	}
	
	public void unZipFile(String compressedPath, String newFilePath)
	{
		File folder = new File(newFilePath);
		if(!folder.exists())
		{
			folder.mkdirs();
		}
		try
		{
			byte[] buffer = new byte[4096];
			ZipInputStream zips = new ZipInputStream(new FileInputStream(compressedPath));
			ZipEntry entry = null;
			String outFileName= null;
			
			while((entry=zips.getNextEntry())!=null)
			{
				String entryName = entry.getName();
				
				if(entryName.contains("\\"))
				{
				String[] ftr = entryName.split("\\\\");
				if(ftr.length>1) 
				{
					//newFilePath=newFilePath+"\\"+entryName.split("\\\\")[0];
					 outFileName = newFilePath+"\\"+entryName.split("\\\\")[ftr.length-2];
	                 System.out.println("Unzip: " + outFileName);
	                 folder  = new File(outFileName);
	                 if(!folder.exists())
	         		{
	         			folder.mkdirs();
	         		}
	                 
	                 outFileName = newFilePath+File.separator +entryName.split("\\\\")[ftr.length-2]+File.separator+entryName.split("\\\\")[ftr.length-1];
	                 
				}
				}else {
                 outFileName = newFilePath + File.separator + entryName;
                System.out.println("Unzip: " + outFileName);
				}
                
                if(entry.isDirectory())
                {
                	new File(outFileName).mkdirs();
                }else
                {
                	FileOutputStream fos = new FileOutputStream(outFileName);
                	
                	int len;
                	
                	while((len = zips.read(buffer))>0)
                	{
                		fos.write(buffer, 0, len);
                	}
                	fos.close();
                }
			}
			
			
		}catch(IOException ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	private void populateFilesList(File dir) throws IOException {
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isFile()) filesListInDir.add(file.getAbsolutePath());
            else populateFilesList(file);
        }
    }
	
	
}
