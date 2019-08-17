package com.nagarro.nagp;

import java.util.Random;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.nagarro.nagp.cache.ProductCacheImpl;
import com.nagarro.nagp.model.Product;
import com.nagarro.nagp.model.RequestHeaderDTO;

@Component
public class InventoryImpl {
	
	@Autowired
	private ProductCacheImpl productCacheImpl;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("jsa.direct3")
	private String exchange3;
	
	@Value("jsa.routingkey3")
	private String routingKey3;
	
	@Value("jsa.direct2")
	private String exchange2;
	
	@Value("jsa.routingkey2")
	private String routingKey2;

	@Autowired
	ProductJDBCRepository jdbcRepo;
	
	@RabbitListener(queues="jsa.queue")
    public void recievedMessage(Message msg) throws JSONException, InterruptedException {
		RequestHeaderDTO req = (RequestHeaderDTO) SerializationUtils
				.deserialize(msg.getBody());
		Product product = findById(req);
		
		if(product!= null) {
		JSONObject json = new JSONObject();
		if(req.getId() == null)
			req.setId(getRandomHexString(16));
		json.put("id", req.getId());
		json.put("name", product.getName());
		json.put("amount", product.getAmount());
		json.put("quantity", req.getQuantity());
		produceMsg(SerializationUtils.serialize(json.toString()));
		updateInventory(product,req);
		System.out.println("REceieved!!!");
		} else {
			JSONObject json = new JSONObject();
			json.put("id", req.getId());
			json.put("name", req.getName());
			json.put("quantity", -1);
			produceMsg(SerializationUtils.serialize(json.toString()));
		}
    }
	
	  private String getRandomHexString(int numchars){
	        Random r = new Random();
	        StringBuffer sb = new StringBuffer();
	        while(sb.length() < numchars){
	            sb.append(Integer.toHexString(r.nextInt()));
	        }

	        return sb.toString().substring(0, numchars);
	    }
	
	public void updateInventory(Product p, RequestHeaderDTO req) {
		//RequestHeaderDTO req =   (RequestHeaderDTO) SerializationUtils.deserialize(req2.getBody());
		//JdbcTemplate j = new JdbcTemplate();
		int q = (p.getQuantity() - req.getQuantity()) > 0 ? p.getQuantity() - req.getQuantity() : 0;
		p.setQuantity(q);
		try {
		  jdbcRepo.getJdbcTemplate().update("update tb_product set quantity = ?" + " where name = ?",
			        new Object[] {
			           q, req.getName()
			        });
		  productCacheImpl.cacheProduct(p);
		}
		catch(Exception e) {
			System.out.println("Database Issue");
		}
}
	
	
	public  Product findById(RequestHeaderDTO req) {
		Product p = productCacheImpl.checkCache(req.getName());
		if (p == null) {
			try {
				p = jdbcRepo.getJdbcTemplate().queryForObject(
						"select * from tb_product where name=?"
								+ " and quantity > ?" + " limit 1",
						new Object[] { req.getName(), req.getQuantity() },
						new BeanPropertyRowMapper<Product>(Product.class));
			     } catch (Exception e) {
				System.out.println("Database Issue");
			  }
		}
		return p;
	}
	
	public void produceMsg(byte[] bs) throws InterruptedException{
		
		amqpTemplate.convertAndSend(exchange2, routingKey2, bs);
		Thread.sleep(2000);
		amqpTemplate.convertAndSend(exchange3, routingKey3, bs);
		System.out.println("Send inventory msg = " + bs);
	
 }
	public String getObject(String id) {
		String s = "";
		try {
			Product o = jdbcRepo.getJdbcTemplate().queryForObject(
					"select * from tb_product where id=?",
					new Object[] { id },
					new BeanPropertyRowMapper<Product>(Product.class));
			s = o.toString();
		} catch (Exception e) {
			s = "No record found";
		}
		return s;
	}
}

