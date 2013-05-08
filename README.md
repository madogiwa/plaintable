# How to Use #
## Table Definition ##
### simple table ###

	@Table(name="User")    // required
	public class UserTable implements SchemaDefinition {
	
		public static Schema schema;    // required
	
		public static SyntheticKey id;  // required
	
		@Attribute(indexed=true, unique=true)
		public static CharAttribute username;
	
		public static CharAttribute password;
	
		@Attribute(length=128, nullable=true)
		public static CharAttribute email;
	
		@Attribute(nullable=true)
		public static IntegerAttribute age;
	
	}

### table has relation ###

	@Table(name="Message")    // required
	public class MessageTable implements SchemaDefinition {
	
		public static Schema schema;    // required
	
		public static SyntheticKey id;  // required
	
		@Reference(target=UserTable.class, cascade=true)    // required
		public static ReferenceKey src;
	
		@Reference(target=UserTable.class, cascade=true)    // required
		public static ReferenceKey dest;
	
		public static TimestampAttribute created;
	
		public static StringAttribute subject;
	
		public static StringAttribute body;
	
	}

## Initialization ##
### create DatabaseManager ###

	DataSource dataSource = getDataSource();    // RDBMS specific
	DatabaseManagerFactory factory = new DatabaseManagerFactory(dataSource);
	DatabaseManager databaseManager = factory.getDatabaseManager();
	databaseManager.setDefaultDelayedOpen(true);
	databaseManager.setDefaultAutoCommit(true);

### register Table schema and execute DDL ###

	SchemaManager schemaManager = databaseManager.getSchemaManager();
	schemaManager.manage(UserTable.class);
	schemaManager.manage(MessageTable.class);

	boolean updated = schemaManager.sync(SynchronizeMode.ALL_DROP_AND_CREATE);
	if (updated) {  // table is created or modified
		// insert initial data, etc...
	}


## Select Query ##

    try {
        Finder finder = new Finder(databaseManager.newSession());
        List<User> users = finder.from(UserTable.schema).exact(UserTable.username, "user1")
                .toList(new BeanMapper<User>(User.class)); // SQL: WHERE username LIKE "user1"
    } catch (PlainTableException e) {
        // error handling
    }

### bean for mapping ###

	@Mapped(schema=UserTable.class)  // specify Table Definition class
	public class User {
	
		private Long id;
	
		private String username;
	
		private String password;
	
		public Long getId() {
			return id;
		}
	
		public void setId(Long id) {
			this.id = id;
		}
	
		public String getUsername() {
			return username;
		}
	
		public void setUsername(String username) {
			this.username = username;
		}
	
		public String getPassword() {
			return password;
		}
	
		public void setPassword(String password) {
			this.password = password;
		}
	
	}

## Insert and Update ##
### insert ###

	Session session = null;
	try {
		session = databaseManager.newSession();
		session.open();

		User user = new User();
		user.setUsername("user1");
		user.setPassword("passwd");

		BeanRowProvider<User> provider = new BeanRowProvider<User>(user);
		long id = session.insert(provider);  // return auto-generated key

		session.commit();
	} catch (PlainTableException e) {
		// error handling
	} finally {
		if (session != null) {
			session.close();
		}
	}

### update ###

    try {
		User user = new User();
		user.setUsername("user1");
		user.setPassword("newpasswd");
		BeanRowProvider<User> provider = new BeanRowProvider<User>(user);

        Finder finder = new Finder(session);
        long count = finder.from(UserTable.schema).eq(UserTable.id, 2).update(provider);
    } catch (PlainTableException e) {
        // error handling
    }


### bean for insert and update ###

	@Mapped(schema=UserTable.class)
	@Provided(schema=UserTable.class)  // mark as provider
	public class User {
	
		private Long id;
	
		private String username;
	
		private String password;
	
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}
	
		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}
	
		/**
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}
	
		/**
		 * @param username the username to set
		 */
		public void setUsername(String username) {
			this.username = username;
		}
	
		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}
	
		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) {
			this.password = password;
		}
	
	}


## Delete ##

    try {
        Finder finder = new Finder(session);
        long count = finder.from(UserTable.schema).contain(UserTable.username, "user1").delete();
    } catch (PlainTableException e) {
        // error handling
    }
