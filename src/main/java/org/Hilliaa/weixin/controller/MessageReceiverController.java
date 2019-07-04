package org.Hilliaa.weixin.controller;

import org.Hilliaa.weixin.domain.InMessage;
import org.Hilliaa.weixin.domain.text.TextInMessage;
import org.Hilliaa.weixin.service.MessageConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hillia/weixin/receiver")
public class MessageReceiverController {
	
	private static final Logger LOG=LoggerFactory.getLogger(MessageReceiverController.class);
	
	@GetMapping
	public String echo(
			@RequestParam("signature") String signature,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("nonce") String nonce,
			@RequestParam("echostr") String echostr
			) {
		return echostr;
	}
	
	@PostMapping
	public String onMessage(
			@RequestParam("signature") String signature,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("nonce") String nonce,
			@RequestBody String xml) {
		LOG.trace("收到的信息原文:\n{}\n-------------------------",xml);
		
		
		//String type = xml.substring(xml.indexOf("<MsgType><![CDATA[") +18);  
		//type = type.substring(0, type.indexOf("]"));
		
		////if(type.equals("text")) {
			//InMessage x = new TextInMessage();
		///}else if(type.equals("image")) {
			//InMessage x = new ImageInMessage();
	
		InMessage inMessage = MessageConvertHelper.convert(xml);
		
		LOG.debug("转换后的消息对象\n{}\n", inMessage);
		
		return "success";
	}
}
