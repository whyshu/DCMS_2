package Server;

import DcmsApp.*;
import org.omg.CosNaming.*;

import java.util.HashMap;

import java.io.File;
import java.io.IOException;

import Conf.Constants;
import org.omg.CORBA.*;
import Conf.ServerCenterLocation.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import Conf.ServerCenterLocation;

public class DcmsServer {
	static HashMap<String, DcmsServerImpl> serverRepo;
	static Dcms mtlhref, lvlhref, ddohref;

	static {
		System.out.println("Starting CORBA Service!");
		try {
			Runtime.getRuntime().exec("orbd -ORBInitialPort 1050 -ORBInitialHost localhost");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void init() {

		boolean isMtlDir = new File(Constants.LOG_DIR + ServerCenterLocation.MTL.toString()).mkdir();
		boolean isLvlDir = new File(Constants.LOG_DIR + ServerCenterLocation.LVL.toString()).mkdir();
		boolean isDdoDir = new File(Constants.LOG_DIR + ServerCenterLocation.DDO.toString()).mkdir();
		boolean globalDir = new File(Constants.LOG_DIR + "ServerGlobal").mkdir();
	}

	public static void main(String args[]) {
		try {

			init();
			ORB orb = ORB.init(args, null);
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			DcmsServerImpl mtlServer = new DcmsServerImpl(ServerCenterLocation.MTL);
			DcmsServerImpl lvlServer = new DcmsServerImpl(ServerCenterLocation.LVL);
			DcmsServerImpl ddoServer = new DcmsServerImpl(ServerCenterLocation.DDO);

			serverRepo = new HashMap<>();
			serverRepo.put("MTL", mtlServer);
			serverRepo.put("LVL", lvlServer);
			serverRepo.put("DDO", ddoServer);

			// get object reference from the servant
			org.omg.CORBA.Object mtlRef = rootpoa.servant_to_reference(mtlServer);
			org.omg.CORBA.Object lvlRef = rootpoa.servant_to_reference(lvlServer);
			org.omg.CORBA.Object ddoRef = rootpoa.servant_to_reference(ddoServer);
			mtlhref = DcmsHelper.narrow(mtlRef);
			lvlhref = DcmsHelper.narrow(lvlRef);
			ddohref = DcmsHelper.narrow(ddoRef);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			NameComponent mtlPath[] = ncRef.to_name("MTL");
			NameComponent lvlPath[] = ncRef.to_name("LVL");
			NameComponent ddoPath[] = ncRef.to_name("DDO");

			ncRef.rebind(mtlPath, mtlhref);
			ncRef.rebind(lvlPath, lvlhref);
			ncRef.rebind(ddoPath, ddohref);

			System.out.println("DCMS Servers ready and waiting ...");
		}

		catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}

	}
}
