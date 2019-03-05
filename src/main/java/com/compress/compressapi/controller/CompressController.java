package com.compress.compressapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.compress.compressapi.bean.CompressBean;
import com.compress.compressapi.bean.DeCompressBean;
import com.compress.compressapi.service.CompressService;

@RestController
public class CompressController {
	
	@Autowired
	CompressService compressService;
	
	@PostMapping("/compress")
	public void compress(@RequestBody CompressBean compressBean) {
		String inputPath=compressBean.getInputPath();
		String outputPath=compressBean.getOutputPath();
		String size=compressBean.getSize();
		
		compressService.zipFolderFile(inputPath, outputPath, size);
		
		
	}
	
	@PostMapping("/deCompress")
	public void deCompress(@RequestBody DeCompressBean deCompressBean) {
		String inputPath=deCompressBean.getInputPath();
		String outputPath=deCompressBean.getOutputPath();
		compressService.unZipFile(inputPath, outputPath);
		
		
	}
	
	

}
