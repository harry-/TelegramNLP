package harry;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "telegramuser")
public class Telegramuser {
    @DatabaseField(id = true)
    private int USERID;
    
    @DatabaseField()
    private String FIRSTNAME;

    @DatabaseField()
    private String SECONDNAME;

    @DatabaseField()
    private String HANDLE;

    @DatabaseField()
    private String GENDER;

    
    Telegramuser() {
    	// all persisted classes must define a no-arg constructor with at least package visibility
    }

		public Telegramuser(int userid, String firstname, String handle) {
			this.USERID = userid;
			this.HANDLE = handle;
			this.FIRSTNAME = firstname;
		}

		public int getUserid() {
			return USERID;
		}

		public void setUserid(int userid) {
			this.USERID=userid;
		}

		public String getHandle() {
			return HANDLE;
		}

		public void setGender(String gender) {
			this.GENDER = gender;
		}

		public String getGender() {
			return GENDER;
		}

		public void setFirstname(String firstname) {
			this.FIRSTNAME = firstname;
		}
		public String getFirstname() {
			return FIRSTNAME;
		}
}