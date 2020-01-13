package com.lorua.domain;

import java.util.Calendar;
import java.util.Date;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

public class PasswordResetToken {
	



		private static final int EXPIRATION = 60 * 24;
		
		@Relationship(type = "HAS_PASSWORD_TOKEN", direction = Relationship.INCOMING)
		private User user;
	
	 	@Id
	    private Long id;
	 	
	 	private String token;
	 	
	 	
	 	private Date expiryDate;

	 	
		public PasswordResetToken(String token, User user) {
			this.user = user;
			this.token = token;
			this.expiryDate = calculateExpiryDate(EXPIRATION);

			
		}

		
		private Date calculateExpiryDate(final int expiryTimeInMinutes) {
			final Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(new Date().getTime());
			cal.add(Calendar.MINUTE, expiryTimeInMinutes);
			return new Date(cal.getTime().getTime());
		}


		public User getUser() {
			return user;
		}


		public void setUser(User user) {
			this.user = user;
		}


		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		public String getToken() {
			return token;
		}


		public void setToken(String token) {
			this.token = token;
		}


		public Date getExpiryDate() {
			return expiryDate;
		}


		public void setExpiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
		}


		public static int getExpiration() {
			return EXPIRATION;
		}

}
