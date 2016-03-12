package edu.ramapo.jkole.cad;

import javafx.application.Platform;

public class CmdLine {
	Call c;
	
	public static void modify(Call selectedCall, String string, String[] str) {
		if(string.equalsIgnoreCase("cc")){
			clearCall(selectedCall);
			try{
				if(str[1].equalsIgnoreCase("allcalls")){
					Call.clearAll();
				}
			}
			catch(ArrayIndexOutOfBoundsException e){}
		}
		else if(string.equalsIgnoreCase("au")){
			ApparatusDispatch.enrtApp(str[1]);
		}
		else if(string.equalsIgnoreCase("ar")){
			ApparatusDispatch.arvdApp(str[1]);
		}
		else if(string.equalsIgnoreCase(";") || string.equalsIgnoreCase(";;")){
			Call.addComment(selectedCall, str);
		}
		else if(str[0].equalsIgnoreCase("disp")){
			try{
				if(str[1].equalsIgnoreCase("init")){
					Dispatch.recUnits(selectedCall.getCall().get("cadid").toString());
				}
				else if(str[1].equalsIgnoreCase("up")){
					FireCallScreen fcs = new FireCallScreen(selectedCall.getCall().get("cadid"));
					System.out.println(fcs.paged.getText());
					try {
						ApparatusDispatch.upgrade();
						fcs.clear();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				System.out.println("MISSING CALL");
			}
		}
		else{
			execute(str);
		}
	}

	private static void clearCall(Call selectedCall) {
		System.out.println("CLEAR CALL "+selectedCall.getCall().get("actid").toString());
		Call.clearCall(selectedCall);
	}
	
	public static void execute(String[] str){
		if(str[0].equalsIgnoreCase("exit")){
			Platform.exit();
		}
		else if(str[0].equalsIgnoreCase("calls")){
			new ActCallMenu();
		}
		else if(str[0].equalsIgnoreCase("cc")){
			try{
				if(str[1].equalsIgnoreCase("allcalls")){
					Call.clearAll();
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				Call.clearCall(ActCallMenu.getSelectedCall());
			}
		}
		else if(str[0].equalsIgnoreCase("calltkr")){
			new CallTakerScreen();
		}
		else if(str[0].equalsIgnoreCase("cl")){
			Apparatus.changeLocation(str[1], str[2]);
		}
		else if(str[0].equalsIgnoreCase("appmenu")){
			try {
				new AppMenu();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(str[0].equalsIgnoreCase("OOS")){
			ApparatusDispatch.setOss(str[1], str);
		}
		else if(str[0].equalsIgnoreCase("stamenu")){
			try {
				new StationScreen(Main.isAdmin());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(str[0].equalsIgnoreCase("munmenu")){
			try {
				new MunicMenu();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(str[0].equalsIgnoreCase("avail")){
			ApparatusDispatch.setAvail(str[1]);
		}
		else if(str[0].equalsIgnoreCase("fcall")){
			new FireCallScreen(str[1]);
		}	
		else if(str[0].equalsIgnoreCase("exitcad")){
			Platform.exit();
		}
	}
}