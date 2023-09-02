//Sangamesh Itagi and Gourish Pisal
package cpsc4620;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuHelper {

    public static String getPizzaCrust(int menuId){
        if(menuId==1) return "Thin";
        else if(menuId==2) return "Original";
        else if(menuId==3) return "Pan";
        else return "Gluten-Free";
    }

    public static String getPizzaSize(int menuId){
        if(menuId==1) return "small";
        else if(menuId==2) return "medium";
        else if(menuId==3) return "large";
        else return "x-large";
    }

	public static String getCurrentTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		return dtf.format(LocalDateTime.now());
	}


}
