import java.lang.reflect.Field;

public abstract class StandardHasher {
	public int hashCode() {
		int myHash = 1;
		for (Field field : this.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				if (field.get(this) != null)
					myHash = 31 * myHash + field.get(this).hashCode();
			} catch (IllegalArgumentException ex) {
		        ex.printStackTrace();
		    } catch (IllegalAccessException ex) {
		        ex.printStackTrace();
		    }
		}
		return myHash;
	}
  
}
