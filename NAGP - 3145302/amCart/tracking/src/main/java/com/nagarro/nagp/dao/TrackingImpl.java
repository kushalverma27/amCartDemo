package com.nagarro.nagp.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.nagarro.nagp.model.RequestHeaderDTO;
import com.nagarro.nagp.model.Tracking;

@Component
public class TrackingImpl {
	

	@Autowired
	TrackingJdbcRepository jdbcRepo;
	
	@Autowired 
	private PlatformTransactionManager platformTransactionManager;
	
	
	@RabbitListener(queues="jsa.queue1")
    public void orderRequestReceivedEvent(Message msg) throws JSONException, InterruptedException {
		RequestHeaderDTO req = (RequestHeaderDTO) SerializationUtils
				.deserialize(msg.getBody());
		if(req.getId() == null)
			req.setId(getRandomHexString(16));
  	  insertOrderDetailsIntoDB(req.getId());
		System.out.println("REceieved!!!");
    }
	
	  private String getRandomHexString(int numchars){
	        Random r = new Random();
	        StringBuffer sb = new StringBuffer();
	        while(sb.length() < numchars){
	            sb.append(Integer.toHexString(r.nextInt()));
	        }

	        return sb.toString().substring(0, numchars);
	    }

	
	private void insertOrderDetailsIntoDB(String string) throws DataAccessException,
			JSONException {
		
		DefaultTransactionDefinition paramTransactionDefinition = new    DefaultTransactionDefinition();

		  TransactionStatus status=platformTransactionManager.getTransaction(paramTransactionDefinition );
	      String sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy").format(new Date());
	     // System.out.println(sdf.format(cal.getTime()));
		  try{
		  jdbcRepo.getJdbcTemplate().getDataSource();
			 jdbcRepo.getJdbcTemplate().update(
					"insert into tb_tracking (id, time_order, time_delivery ) "
							+ "values(?,  ?, ?)",
					new Object[] {
							string, sdf, null});
		  platformTransactionManager.commit(status);
		}
		catch (Exception e) {
		  platformTransactionManager.rollback(status);
		}
		

	}
	
	
	@RabbitListener(queues = "jsa.queue3")
	public void inventoryAvailableEvent(Message msg) throws JSONException,
			InterruptedException {
		String object = (String) SerializationUtils.deserialize(msg.getBody());
		JSONObject j = new JSONObject(object);
		Thread.sleep(3000);
		Tracking track = findById(j);
		// insertOrderDetailsIntoDB(j);
		insertIntoDB(track,j);
		System.out.println("REceieved Inventory msg!!!");
	}
	
	private void insertIntoDB(Tracking track, JSONObject j) throws DataAccessException, JSONException {
		
		String time = "Item not available";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 5);
		String sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy").format(cal.getTimeInMillis());
		if(track!= null && (int)j.get("quantity")>0)
		time = sdf;
		try {
		
		 jdbcRepo.getJdbcTemplate().update("update tb_tracking set time_delivery = ? where id = ?",
		new Object[] {
				// o.getId(), o.getName(), o.getPassportNumber()
				time, track.getId()
				 });
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Database issue");
		}

}
	
	
	
	public Tracking findById(JSONObject req) throws DataAccessException,JSONException {
		Tracking t = null;
		try {
			t = jdbcRepo.getJdbcTemplate().queryForObject(
					"select * from tb_tracking where id=?",
					new Object[] { req.get("id") },
					new BeanPropertyRowMapper<Tracking>(Tracking.class));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("database issue");
		}
		return t;
	}
	
	public String getObject(String id) {
		String s = "";
		try {
			Tracking o = jdbcRepo.getJdbcTemplate().queryForObject(
					"select * from tb_tracking where id=?",
					new Object[] { id },
					new BeanPropertyRowMapper<Tracking>(Tracking.class));
			s = o.toString();
		} catch (Exception e) {
			s = "No record found";
		}
		return s;
	}



	
}