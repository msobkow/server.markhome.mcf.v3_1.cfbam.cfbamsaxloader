// Description: Java 25 XML SAX Parser for CFBam.

/*
 *	server.markhome.mcf.CFBam
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal CFBam 3.1 Business Application Model
 *	
 *	Copyright 2016-2026 Mark Stephen Sobkow
 *	
 *	This file is part of Mark's Code Fractal CFBam.
 *	
 *	Mark's Code Fractal CFBam is available under dual commercial license from
 *	Mark Stephen Sobkow, or under the terms of the GNU General Public License,
 *	Version 3 or later with classpath and static linking exceptions.
 *	
 *	As a special exception, Mark Sobkow gives you permission to link this library
 *	with independent modules to produce an executable, provided that none of them
 *	conflict with the intent of the GPLv3; that is, you are not allowed to invoke
 *	the methods of this library from non-GPLv3-compatibly licensed code. You may not
 *	implement an LPGLv3 "wedge" to try to bypass this restriction. That said, code which
 *	does not rely on this library is free to specify whatever license its authors decide
 *	to use. Mark Sobkow specifically rejects the infectious nature of the GPLv3, and
 *	considers the mere act of including GPLv3 modules in an executable to be perfectly
 *	reasonable given tools like modern Java's single-jar deployment options.
 *	
 *	Mark's Code Fractal CFBam is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	Mark's Code Fractal CFBam is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with Mark's Code Fractal CFBam.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	If you wish to modify and use this code without publishing your changes,
 *	or integrate it with proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
 */

package server.markhome.mcf.v3_1.cfbam.cfbamsaxloader;

import java.io.File;
import java.time.*;
import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;
import server.markhome.mcf.v3_1.cflib.inz.Inz;
import server.markhome.mcf.v3_1.cflib.xml.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfbam.cfbam.*;
import server.markhome.mcf.v3_1.cfsec.cfsecobj.*;
import server.markhome.mcf.v3_1.cfint.cfintobj.*;
import server.markhome.mcf.v3_1.cfbam.cfbamobj.*;

public class CFBamSaxLdr
{
	protected ICFLibMessageLog log = null;
	protected CFBamSaxLoader saxLoader = null;
	protected String clusterName = "system";
	protected ICFSecClusterObj clusterObj = null;
	protected String tenantName = "system";
	protected static ICFSecTenantObj tenantObj = null;
	protected String secUserName = "system";
	protected ICFSecSecUserObj secUserObj = null;
	protected ICFSecSecSessionObj secSessionObj = null;
	// Constructors

	public CFBamSaxLdr() {
		log = null;
		getSaxLoader();
	}

	public CFBamSaxLdr( ICFLibMessageLog messageLog ) {
		log = messageLog;
		getSaxLoader();
	}

	// Accessors

	public void setSaxLoader( CFBamSaxLoader value ) {
		saxLoader = value;
	}

	public CFBamSaxLoader getSaxLoader() {
		if( saxLoader == null ) {
			saxLoader = new CFBamSaxLoader( log );
		}
		return( saxLoader );
	}

	public void setClusterName( String value ) {
		final String S_ProcName = "setClusterName";
		ICFBamSchemaObj schema = saxLoader.getSchemaObj();
		if( value == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				1,
				"value" );
		}
		else if( value.equals( "default" ) ) {
			try {
				ICFSecSysClusterObj sysCluster = schema.getSysClusterTableObj().readSysClusterByIdIdx( 1 );
				if( sysCluster == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"sysCluster" );
				}
				ICFSecClusterObj useCluster = sysCluster.getRequiredContainerCluster();
				if( useCluster == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"sysCluster.containerCluster" );
				}
				clusterName = value;
				clusterObj = useCluster;
				saxLoader.getSchemaObj().setSecCluster( useCluster );
			}
			catch( RuntimeException e ) {
				throw e;
			}
		}
		else if( value.equals( "system" ) ) {
			try {
				ICFSecClusterObj useCluster = schema.getClusterTableObj().readClusterByUDomNameIdx( "system" );
				if( useCluster == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"readClusterByUDomName-system" );
				}
				clusterName = value;
				clusterObj = useCluster;
				saxLoader.getSchemaObj().setSecCluster( useCluster );
			}
			catch( RuntimeException e ) {
				throw e;
			}
		}
		else {
			throw new CFLibUsageException( getClass(),
				S_ProcName,
				Inz.x("cflib.xml.CFLibXmlUtil.XmlExpectedDefaultOrSystem"),
				Inz.s("cflib.xml.CFLibXmlUtil.XmlExpectedDefaultOrSystem"));
		}
	}

	public String getClusterName() {
		return( clusterName );
	}

	public ICFSecClusterObj getClusterObj() {
		final String S_ProcName = "getClusterObj";
		if( clusterObj == null ) {
			ICFBamSchemaObj schema = saxLoader.getSchemaObj();
			if( clusterName.equals( "default" ) ) {
				try {
					ICFSecSysClusterObj sysCluster = schema.getSysClusterTableObj().readSysClusterByIdIdx( 1 );
					if( sysCluster == null ) {
						throw new CFLibNullArgumentException( getClass(),
							S_ProcName,
							0,
							"sysCluster" );
					}
					ICFSecClusterObj useCluster = sysCluster.getRequiredContainerCluster();
					if( useCluster == null ) {
						throw new CFLibNullArgumentException( getClass(),
							S_ProcName,
							0,
							"sysCluster.containerCluster" );
					}
					clusterObj = useCluster;
					saxLoader.getSchemaObj().setSecCluster( useCluster );
				}
				catch( RuntimeException e ) {
					throw e;
				}
			}
			else if( clusterName.equals( "system" ) ) {
				try {
					ICFSecClusterObj useCluster = schema.getClusterTableObj().readClusterByUDomNameIdx( "system" );
					if( useCluster == null ) {
						throw new CFLibNullArgumentException( getClass(),
							S_ProcName,
							0,
							"readClusterByUDomName-system" );
					}
					clusterObj = useCluster;
					saxLoader.getSchemaObj().setSecCluster( useCluster );
				}
				catch( RuntimeException e ) {
					throw e;
				}
			}
		}
		if( clusterObj == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"clusterObj" );
		}
		return( clusterObj );
	}


	public void setTenantName( String value ) {
		final String S_ProcName = "setTenantName";
		ICFBamSchemaObj schema = saxLoader.getSchemaObj();
		if( value == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				1,
				"value" );
		}
		else if( value.equals( "system" ) ) {
			try {
				ICFSecTenantObj useTenant = schema.getTenantTableObj().readTenantByUNameIdx( clusterObj.getRequiredId(),
					"system" );
				if( useTenant == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"readTenantByUNameIdx-" + clusterObj.getObjName() + "-system" );
				}
				tenantName = value;
				tenantObj = useTenant;
				saxLoader.getSchemaObj().setSecTenant( useTenant );
			}
			catch( RuntimeException e ) {
				throw e;
			}
		}
		else {
			if( clusterObj == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"clusterObj" );
			}
			if( clusterName.equals( "system" ) ) {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlOnlyTenantSystemAllowed"), tenantName),
					String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlOnlyTenantSystemAllowed"), tenantName) );
			}
			try {
				ICFSecTenantObj useTenant = schema.getTenantTableObj().readTenantByUNameIdx( clusterObj.getRequiredId(),
					value );
				if( useTenant == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"readTenantByUNameIdx-" + clusterObj.getObjName() + "-" + value );
				}
				tenantName = value;
				tenantObj = useTenant;
				saxLoader.getSchemaObj().setSecTenant( useTenant );
			}
			catch( RuntimeException e ) {
				throw e;
			}
		}
	}

	public String getTenantName() {
		return( tenantName );
	}

	public ICFSecTenantObj getTenantObj() {
		final String S_ProcName = "getTenantObj";
		if( tenantObj == null ) {
			ICFBamSchemaObj schema = saxLoader.getSchemaObj();
			if( tenantName == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					1,
					"value" );
			}
			else if( tenantName.equals( "system" ) ) {
				try {
					ICFSecTenantObj useTenant = schema.getTenantTableObj().readTenantByUNameIdx( clusterObj.getRequiredId(),
						"system" );
					if( useTenant == null ) {
						throw new CFLibNullArgumentException( getClass(),
							S_ProcName,
							0,
							"readTenantByUNameIdx-" + clusterObj.getObjName() + "-system" );
					}
					tenantObj = useTenant;
					saxLoader.getSchemaObj().setSecTenant( useTenant );
				}
				catch( RuntimeException e ) {
					throw e;
				}
			}
			else {
				if( clusterObj == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"clusterObj" );
				}
				if( clusterName.equals( "system" ) ) {
					throw new CFLibUsageException( getClass(),
						S_ProcName,
						String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlOnlyTenantSystemAllowed"), tenantName),
						String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlOnlyTenantSystemAllowed"), tenantName) );
				}
				try {
					ICFSecTenantObj useTenant = schema.getTenantTableObj().readTenantByUNameIdx( clusterObj.getRequiredId(),
						tenantName );
					if( useTenant == null ) {
						throw new CFLibNullArgumentException( getClass(),
							S_ProcName,
							0,
							"readTenantByUNameIdx-" + clusterObj.getObjName() + "-" + tenantName );
					}
					tenantObj = useTenant;
					saxLoader.getSchemaObj().setSecTenant( useTenant );
				}
				catch( RuntimeException e ) {
					throw e;
				}
			}
		}
		if( tenantObj == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"tenantObj-" + tenantName );
		}
		return( tenantObj );
	}


	public void setSecUserName( String value ) {
		secUserName = value;
	}

	public String getSecUserName() {
		return( secUserName );
	}

	public ICFSecSecUserObj getSecUserObj() {
		if( secUserObj == null ) {
			ICFBamSchemaObj schema = saxLoader.getSchemaObj();
			try {
				secUserObj = schema.getSecUserTableObj().readSecUserByULoginIdx( secUserName );
			}
			catch( RuntimeException e ) {
				throw e;
			}
		}
		if( secUserObj == null ) {
			throw new RuntimeException( "SecUser \"" + secUserName + "\" could not be found" );
		}
		return( secUserObj );
	}


	public ICFSecSecSessionObj getSecSessionObj() {
		if( secSessionObj == null ) {
			ICFBamSchemaObj schema = saxLoader.getSchemaObj();
			try {
				getClusterObj();
				getTenantObj();
				getSecUserObj();
				secSessionObj = schema.getSecSessionTableObj().newInstance();
				ICFSecSecSessionEditObj sessionEdit = secSessionObj.beginEdit();
				sessionEdit.setRequiredSecUserId( secUserObj.getPKey() );
				sessionEdit.setRequiredStart( LocalDateTime.now() );
				sessionEdit.setOptionalFinish( null );
				secSessionObj = sessionEdit.create();
				sessionEdit = null;
			}
			catch( RuntimeException e ) {
				throw e;
			}
		}
		return( secSessionObj );
	}

	// Apply the loader options argument to the specified loader

	protected static void applyLoaderOptions( CFBamSaxLoader loader, String loaderOptions ) {
		final String S_ProcName = "CFBamSaxLdr.applyLoaderOptions() ";
		while( loaderOptions.length() > 0 ) {
			String evalSegment;
			int sepIndex = loaderOptions.indexOf( File.pathSeparatorChar );
			if( sepIndex >= 0 ) {
				evalSegment = loaderOptions.substring( 0, sepIndex );
				if( loaderOptions.length() > sepIndex + 1 ) {
					loaderOptions = loaderOptions.substring( sepIndex + 1 );
				}
				else {
					loaderOptions = "";
				}
			}
			else {
				evalSegment = loaderOptions;
				loaderOptions = "";
			}
			evalLoaderSegment( loader, evalSegment );
		}
	}

	// Evaluate a loader options argument segment

	protected static void evalLoaderSegment( CFBamSaxLoader loader, String evalSegment ) {
		final String S_ProcName = "CFBamSaxParserCLI.evalLoaderSegment() ";
		int sepEquals = evalSegment.indexOf( '=' );
		if( sepEquals <= 0 ) {
			throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
		}
		String tableName = evalSegment.substring( 0, sepEquals );
		String tableOption = evalSegment.substring( sepEquals + 1 );
		if( tableName.equals( "Atom" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setAtomLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setAtomLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setAtomLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "BlobCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setBlobColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setBlobColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setBlobColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "BlobDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setBlobDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setBlobDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setBlobDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "BlobType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setBlobTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setBlobTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setBlobTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "BoolCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setBoolColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setBoolColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setBoolColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "BoolDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setBoolDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setBoolDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setBoolDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "BoolType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setBoolTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setBoolTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setBoolTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Chain" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setChainLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setChainLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setChainLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ClearDep" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setClearDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setClearDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setClearDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ClearSubDep1" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setClearSubDep1LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setClearSubDep1LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setClearSubDep1LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ClearSubDep2" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setClearSubDep2LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setClearSubDep2LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setClearSubDep2LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ClearSubDep3" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setClearSubDep3LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setClearSubDep3LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setClearSubDep3LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ClearTopDep" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setClearTopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setClearTopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setClearTopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Cluster" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setClusterLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setClusterLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setClusterLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DateCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDateColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDateColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDateColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DateDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDateDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDateDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDateDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DateType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDateTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDateTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDateTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash128Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash128ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash128ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash128ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash128Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash128DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash128DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash128DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash128Gen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash128GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash128GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash128GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash128Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash128TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash128TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash128TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash160Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash160ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash160ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash160ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash160Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash160DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash160DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash160DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash160Gen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash160GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash160GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash160GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash160Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash160TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash160TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash160TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash224Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash224ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash224ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash224ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash224Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash224DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash224DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash224DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash224Gen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash224GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash224GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash224GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash224Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash224TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash224TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash224TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash256Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash256ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash256ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash256ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash256Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash256DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash256DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash256DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash256Gen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash256GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash256GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash256GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash256Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash256TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash256TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash256TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash384Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash384ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash384ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash384ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash384Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash384DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash384DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash384DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash384Gen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash384GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash384GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash384GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash384Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash384TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash384TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash384TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash512Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash512ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash512ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash512ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash512Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash512DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash512DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash512DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash512Gen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash512GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash512GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash512GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DbKeyHash512Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDbKeyHash512TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDbKeyHash512TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDbKeyHash512TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DelDep" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDelDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDelDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDelDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DelSubDep1" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDelSubDep1LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDelSubDep1LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDelSubDep1LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DelSubDep2" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDelSubDep2LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDelSubDep2LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDelSubDep2LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DelSubDep3" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDelSubDep3LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDelSubDep3LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDelSubDep3LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DelTopDep" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDelTopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDelTopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDelTopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DoubleCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDoubleColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDoubleColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDoubleColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DoubleDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDoubleDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDoubleDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDoubleDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "DoubleType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setDoubleTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setDoubleTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setDoubleTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "EnumDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setEnumDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setEnumDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setEnumDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "EnumTag" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setEnumTagLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setEnumTagLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setEnumTagLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "EnumType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setEnumTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setEnumTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setEnumTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "FloatCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setFloatColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setFloatColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setFloatColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "FloatDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setFloatDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setFloatDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setFloatDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "FloatType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setFloatTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setFloatTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setFloatTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ISOCcy" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setISOCcyLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setISOCcyLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setISOCcyLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ISOCtry" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setISOCtryLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setISOCtryLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setISOCtryLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ISOCtryCcy" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setISOCtryCcyLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setISOCtryCcyLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setISOCtryCcyLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ISOCtryLang" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setISOCtryLangLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setISOCtryLangLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setISOCtryLangLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ISOLang" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setISOLangLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setISOLangLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setISOLangLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ISOTZone" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setISOTZoneLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setISOTZoneLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setISOTZoneLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Id16Gen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setId16GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setId16GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setId16GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Id32Gen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setId32GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setId32GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setId32GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Id64Gen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setId64GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setId64GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setId64GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Index" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setIndexLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setIndexLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setIndexLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "IndexCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setIndexColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setIndexColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setIndexColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Int16Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setInt16ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setInt16ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setInt16ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Int16Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setInt16DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setInt16DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setInt16DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Int16Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setInt16TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setInt16TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setInt16TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Int32Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setInt32ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setInt32ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setInt32ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Int32Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setInt32DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setInt32DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setInt32DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Int32Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setInt32TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setInt32TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setInt32TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Int64Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setInt64ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setInt64ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setInt64ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Int64Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setInt64DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setInt64DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setInt64DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Int64Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setInt64TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setInt64TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setInt64TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "License" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setLicenseLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setLicenseLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setLicenseLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "MajorVersion" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setMajorVersionLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setMajorVersionLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setMajorVersionLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "MimeType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setMimeTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setMimeTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setMimeTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "MinorVersion" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setMinorVersionLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setMinorVersionLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setMinorVersionLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "NmTokenCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setNmTokenColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setNmTokenColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setNmTokenColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "NmTokenDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setNmTokenDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setNmTokenDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setNmTokenDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "NmTokenType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setNmTokenTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setNmTokenTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setNmTokenTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "NmTokensCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setNmTokensColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setNmTokensColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setNmTokensColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "NmTokensDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setNmTokensDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setNmTokensDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setNmTokensDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "NmTokensType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setNmTokensTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setNmTokensTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setNmTokensTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "NumberCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setNumberColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setNumberColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setNumberColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "NumberDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setNumberDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setNumberDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setNumberDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "NumberType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setNumberTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setNumberTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setNumberTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Param" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setParamLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setParamLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setParamLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "PopDep" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setPopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setPopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setPopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "PopSubDep1" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setPopSubDep1LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setPopSubDep1LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setPopSubDep1LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "PopSubDep2" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setPopSubDep2LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setPopSubDep2LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setPopSubDep2LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "PopSubDep3" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setPopSubDep3LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setPopSubDep3LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setPopSubDep3LoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "PopTopDep" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setPopTopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setPopTopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setPopTopDepLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Relation" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setRelationLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setRelationLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setRelationLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "RelationCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setRelationColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setRelationColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setRelationColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SchemaDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSchemaDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSchemaDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSchemaDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SchemaRef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSchemaRefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSchemaRefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSchemaRefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SchemaTweak" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSchemaTweakLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSchemaTweakLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSchemaTweakLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Scope" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setScopeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setScopeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setScopeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecClusGrp" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecClusGrpLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecClusGrpLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecClusGrpLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecClusGrpInc" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecClusGrpIncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecClusGrpIncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecClusGrpIncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecClusGrpMemb" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecClusGrpMembLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecClusGrpMembLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecClusGrpMembLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecSession" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecSessionLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecSessionLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecSessionLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecSysGrp" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecSysGrpLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecSysGrpLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecSysGrpLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecSysGrpInc" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecSysGrpIncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecSysGrpIncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecSysGrpIncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecSysGrpMemb" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecSysGrpMembLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecSysGrpMembLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecSysGrpMembLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecTentGrp" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecTentGrpLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecTentGrpLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecTentGrpLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecTentGrpInc" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecTentGrpIncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecTentGrpIncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecTentGrpIncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecTentGrpMemb" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecTentGrpMembLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecTentGrpMembLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecTentGrpMembLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecUser" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecUserLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecUserLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecUserLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecUserPWHistory" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecUserPWHistoryLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecUserPWHistoryLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecUserPWHistoryLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SecUserPassword" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSecUserPasswordLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSecUserPasswordLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSecUserPasswordLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ServerListFunc" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setServerListFuncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setServerListFuncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setServerListFuncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ServerMethod" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setServerMethodLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setServerMethodLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setServerMethodLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ServerObjFunc" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setServerObjFuncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setServerObjFuncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setServerObjFuncLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "ServerProc" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setServerProcLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setServerProcLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setServerProcLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "StringCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setStringColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setStringColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setStringColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "StringDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setStringDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setStringDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setStringDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "StringType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setStringTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setStringTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setStringTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SubProject" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSubProjectLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSubProjectLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSubProjectLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "SysCluster" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setSysClusterLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setSysClusterLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setSysClusterLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TZDateCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTZDateColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTZDateColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTZDateColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TZDateDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTZDateDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTZDateDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTZDateDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TZDateType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTZDateTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTZDateTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTZDateTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TZTimeCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTZTimeColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTZTimeColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTZTimeColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TZTimeDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTZTimeDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTZTimeDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTZTimeDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TZTimeType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTZTimeTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTZTimeTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTZTimeTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TZTimestampCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTZTimestampColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTZTimestampColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTZTimestampColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TZTimestampDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTZTimestampDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTZTimestampDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTZTimestampDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TZTimestampType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTZTimestampTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTZTimestampTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTZTimestampTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Table" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTableLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTableLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTableLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TableCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTableColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTableColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTableColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TableTweak" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTableTweakLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTableTweakLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTableTweakLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Tenant" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTenantLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTenantLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTenantLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TextCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTextColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTextColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTextColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TextDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTextDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTextDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTextDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TextType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTextTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTextTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTextTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TimeCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTimeColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTimeColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTimeColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TimeDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTimeDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTimeDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTimeDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TimeType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTimeTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTimeTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTimeTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TimestampCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTimestampColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTimestampColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTimestampColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TimestampDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTimestampDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTimestampDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTimestampDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TimestampType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTimestampTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTimestampTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTimestampTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Tld" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTldLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTldLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTldLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TokenCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTokenColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTokenColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTokenColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TokenDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTokenDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTokenDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTokenDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TokenType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTokenTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTokenTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTokenTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TopDomain" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTopDomainLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTopDomainLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTopDomainLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "TopProject" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTopProjectLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTopProjectLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTopProjectLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Tweak" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setTweakLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setTweakLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setTweakLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UInt16Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUInt16ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUInt16ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUInt16ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UInt16Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUInt16DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUInt16DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUInt16DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UInt16Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUInt16TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUInt16TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUInt16TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UInt32Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUInt32ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUInt32ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUInt32ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UInt32Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUInt32DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUInt32DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUInt32DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UInt32Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUInt32TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUInt32TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUInt32TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UInt64Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUInt64ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUInt64ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUInt64ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UInt64Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUInt64DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUInt64DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUInt64DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UInt64Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUInt64TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUInt64TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUInt64TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "URLProtocol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setURLProtocolLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setURLProtocolLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setURLProtocolLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Uuid6Col" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUuid6ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUuid6ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUuid6ColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Uuid6Def" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUuid6DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUuid6DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUuid6DefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Uuid6Gen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUuid6GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUuid6GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUuid6GenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Uuid6Type" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUuid6TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUuid6TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUuid6TypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UuidCol" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUuidColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUuidColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUuidColLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UuidDef" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUuidDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUuidDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUuidDefLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UuidGen" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUuidGenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUuidGenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUuidGenLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "UuidType" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setUuidTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setUuidTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setUuidTypeLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else if( tableName.equals( "Value" ) ) {
			if( tableOption.equals( "*" ) ) {
				// Leave at default
			}
			else if( tableOption.equals( "Insert" ) ) {
				loader.setValueLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Insert );
			}
			else if( tableOption.equals( "Update" ) ) {
				loader.setValueLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Update );
			}
			else if( tableOption.equals( "Replace" ) ) {
				loader.setValueLoaderBehaviour( CFBamSaxLoader.LoaderBehaviourEnum.Replace );
			}
			else {
				throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
			}
		}
		else {
			throw new RuntimeException( S_ProcName + "ERROR: Expected segment to comprise <TableName>={*|Insert|Update|Replace}" );
		}
	}

	// Evaluate remaining arguments

	public void evaluateRemainingArgs( String[] args, int consumed ) {
		// This method gets overloaded to evaluate the database-specific
		// connection arguments, if supported by a database specialization.
	}

}
