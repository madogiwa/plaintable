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


## SELECT ##

### bean for SELECT ###

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


    @Mapped(schema=MessageTable.class)  // specify Table Definition class
    public class Message {

        private Long id;

        private User src;

        private User dest;

        private Date created;

        private String subject;

        private String body;

        public Long getId() {
          return id;
        }

        public void setId(Long id) {
          this.id = id;
        }

        public User getSrc() {
          return src;
        }

        public void setSrc(User src) {
          this.src = src;
        }

        public User getDest() {
          return dest;
        }

        public void setDest(User dest) {
          this.dest = dest;
        }

        public Date getCreated() {
          return created;
        }

        public void setCreated(Date created) {
          this.created = created;
        }

        public String getSubject() {
          return subject;
        }

        public void setSubject(String subject) {
          this.subject = subject;
        }

        public String getBody() {
          return body;
        }

        public void setBody(String body) {
          this.body = body;
        }

    }

### SELECT(SELECT *, COUNT, JOIN...) ###

    try {
        Finder finder = new Finder(databaseManager.newSession());

        // SQL: SELECT * FROM User WHERE username LIKE 'user1';
        List<User> users = finder.from(UserTable.schema).exact(UserTable.username, "user1")
                .toList(User.class);

        // SQL: SELECT COUNT(DISTINCT Message.id) FROM Message LEFT OUTER JOIN User ON Message.src = User.id LEFT OUTER JOIN User ON Message.dest = User.id
        Rows rows = finder.from(MessageTable.schema).outerJoin(MessageTable.src, UserTable.id).outerJoin(MessageTable.dest, UserTable.id);
        long count = rows.count();

        // SQL: SELECT * FROM Message LEFT OUTER JOIN User ON Message.src = User.id LEFT OUTER JOIN User ON Message.dest = User.id
        List<Message> msgList = rows.toList(Message.class);
    } catch (PlainTableException e) {
        // error handling
    }

## INSERT and UPDATE ##
### bean for INSERT and UPDATE ###

	@Mapped(schema=UserTable.class)
	@Provided(schema=UserTable.class)  // mark as provider
	public class User {
		...


	@Mapped(schema=MessageTable.class)
	@Provided(schema=MessageTable.class)  // mark as provider
	public class Message {
	    ...


### INSERT ###

    try {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("newpasswd");

        Finder finder = new Finder(session);
        // SQL: INSERT INTO User(username, password) VALUES('user1', 'newpassword')
        Long id = finder.insert(user).getId();
    } catch (PlainTableException e) {
        // error handling
    }

### UPDATE ###

    try {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("newpasswd");

        Finder finder = new Finder(session);
        // SQL: UPDATE User SET username='user1', password='newpassword' WHERE id = 2
        long count = finder.from(UserTable.schema).eq(UserTable.id, 2).update(user);
    } catch (PlainTableException e) {
        // error handling
    }


## DELETE ##

    try {
        Finder finder = new Finder(session);
        // SQL: DELETE FROM User WHERE username LIKE '%user1%'
        long count = finder.from(UserTable.schema).contain(UserTable.username, "user1").delete();
    } catch (PlainTableException e) {
        // error handling
    }
