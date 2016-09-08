package com.example.repository;

import com.example.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.util.List;

@Repository
@Transactional
public class CustomerRepository {
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;
	
	//For Insert
	SimpleJdbcInsert insert;
	
	@PostConstruct
	public void init() {
		insert = new SimpleJdbcInsert((JdbcTemplate) jdbcTemplate.getJdbcOperations())
			.withTableName("Customers")
			.usingGeneratedKeyColumns("id");
	}
	
	private static final RowMapper<Customer> customerRowMapper = (rs, i) -> {
		Integer id = rs.getInt("id");
		String firstName = rs.getString("first_name");
		String lastName = rs.getString("last_name");
		return new Customer(id, firstName, lastName);
	};
	
	public List<Customer> findAll() {
		List<Customer> customers = jdbcTemplate.query(
				"SELECT id,first_name,last_name FROM Customers ORDER BY id", 
				customerRowMapper);
		return customers;
	}
	
	public Customer findOne(Integer id) {
		SqlParameterSource param = new MapSqlParameterSource().addValue("id",  id);
		return jdbcTemplate.queryForObject(
				"SELECT id,first_name,last_name FROM Customers WHERE id = :id", 
				param, 
				customerRowMapper);
	}
	
	public Customer save(Customer customer) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(customer);
		if (customer.getId() == null) {
			//jdbcTemplate.update("INSERT INTO Customers(first_name, last_name) values(:firstName, :lastName)", param);
			Number key = insert.executeAndReturnKey(param);
			customer.setId(key.intValue());
		} else {
			jdbcTemplate.update("UPDATE Customers SET first_name = :firstName, last_name = :last_name WHERE id = :id", param);
		}
		return customer;
	}
	
	public void delete(Integer id) {
		SqlParameterSource param = new MapSqlParameterSource().addValue("id",  id);
		jdbcTemplate.update("DELETE FROM Customers WHERE id = :id", param);
	}
}
