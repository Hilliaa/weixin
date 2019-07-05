package org.Hilliaa.weixin.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.Hilliaa.weixin.domain.InMessage;
import org.Hilliaa.weixin.domain.text.TextInMessage;
import org.Hilliaa.weixin.service.MessageConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@RestController
@RequestMapping("/hillia/weixin/receiver")
public class MessageReceiverController {
	
	
	@Autowired
	private XmlMapper xmlMapper;
	
	@Autowired
	private RedisTemplate<String, ? extends InMessage> inMessageTemplate;
	
	
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
			@RequestBody String xml) throws JsonParseException, JsonMappingException, IOException {
		LOG.trace("收到的信息原文:\n{}\n-------------------------",xml);
		
		
		//String type = xml.substring(xml.indexOf("<MsgType><![CDATA[") +18);  
		//type = type.substring(0, type.indexOf("]"));
		
		////if(type.equals("text")) 123{
			//InMessage x = new TextInMessage();
		///}else if(type.equals("image")) {
			//InMessage x = new ImageInMessage();
	
		//InMessage inMessage = MessageConvertHelper.convert(xml);
		
		InMessage inMessage = convert(xml);
		
		if(inMessage == null) {
			LOG.error("消息无法转换！原文：\n{}\n",xml);
			return "success";
		}
		
		LOG.debug("转换后的消息对象\n{}\n", inMessage);
		
		String channel = "hillia_" + inMessage.getMsgType();
		
		//ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//ObjectOutputStream out = new ObjectOutputStream(bos);
		//out.writeObject(inMessage);
		
		//byte[] data = bos.toByteArray();
		
		
		//inMessageTemplate.execute(new RedisCallback<InMessage>() {

			//@Override
			//public InMessage doInRedis(RedisConnection connection) throws DataAccessException {
			
				//
				//connection.publish(channel.getBytes(),data);
				//return null;
			//}
			
		//});
		
		
		inMessageTemplate.convertAndSend(channel, inMessage);
		
		return "success";
	}

	private InMessage convert(String xml) throws JsonParseException, JsonMappingException, IOException {
		Class<? extends InMessage> c = MessageConvertHelper.getClass(xml);
		InMessage msg = xmlMapper.readValue(xml, c);
		return msg;
	}
}
