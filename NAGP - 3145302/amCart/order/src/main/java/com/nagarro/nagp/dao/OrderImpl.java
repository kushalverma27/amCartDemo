package com.nagarro.nagp.dao;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.nagarro.nagp.model.Order;

@Component
public class OrderImpl {
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("jsa.direct")
	private String exchange;
	
	@Value("jsa.routingkey")
	private String routingKey;
	
	@Value("jsa.direct1")
	private String exchange1;
	
	@Value("jsa.routingkey1")
	private String routingKey1;
	
	@Autowired
	OrderJdbcRepository jdbcRepo;
	
	
	public void produceMsg(byte[] bs) throws InterruptedException{
		//amqpTemplate.receiveAndReply(queueName, callback, replyExchange, replyRoutingKey)
		amqpTemplate.convertAndSend(exchange, routingKey, bs);
		Thread.sleep(2000);
		amqpTemplate.convertAndSend(exchange1, routingKey1, bs);
		System.out.println("Send msg = " + bs);
	
 }
	
	@RabbitListener(queues="jsa.queue2")
    public void recievedMessage(Message msg) throws JSONException {
		String object =   (String)SerializationUtils.deserialize(msg.getBody());
  	  JSONObject j = new JSONObject(object);
  	  insertIntoDB(j);
		System.out.println("REceieved!!!");
    }
	
	private void insertIntoDB(JSONObject j) throws DataAccessException,
			JSONException {
		try {
		 jdbcRepo.getJdbcTemplate().update(
				"insert into tb_order (id, name, amount, quantity ) "
						+ "values(?,  ?, ?, ?)",
				new Object[] {
						// o.getId(), o.getName(), o.getPassportNumber()
						j.get("id"), j.get("name"), j.get("amount"),j.get("quantity") });
		}
		catch(Exception e) {
			 jdbcRepo.getJdbcTemplate().update(
						"insert into tb_order (id, name, amount, quantity ) "
								+ "values(?,  ?, ?, ?)",
						new Object[] {
								// o.getId(), o.getName(), o.getPassportNumber()
								j.get("id"), j.get("name"), -1, j.get("quantity") });
			System.out.println("database issue");
		}

	}

	public String getObject(String id) {
		String s = "";
		try {
			Order o = jdbcRepo.getJdbcTemplate().queryForObject(
					"select * from tb_order where id=?",
					new Object[] { id },
					new BeanPropertyRowMapper<Order>(Order.class));
			s = o.toString();
		} catch (Exception e) {
			s = "No record found";
		}
		return s;
	}
	
}