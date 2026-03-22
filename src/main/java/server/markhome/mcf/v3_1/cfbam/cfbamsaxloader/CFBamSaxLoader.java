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
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.*;
import java.math.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.naming.*;
import javax.sql.*;
import org.apache.commons.codec.binary.Base64;
import org.xml.sax.*;

import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.xml.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfbam.cfbam.*;
import server.markhome.mcf.v3_1.cfsec.cfsecobj.*;
import server.markhome.mcf.v3_1.cfint.cfintobj.*;
import server.markhome.mcf.v3_1.cfbam.cfbamobj.*;

public class CFBamSaxLoader
	extends CFLibXmlCoreSaxParser
	implements ContentHandler
{

	// The namespace URI of the supported schema
	public final static String	SCHEMA_XMLNS = "http://mcf.markhome.server/mcf/v3_1/xsd/cfbam-structured.xsd";

	// The source for loading the supported schema
	public final static String	SCHEMA_URI = "http://mcf.markhome.server/mcf/v3_1/xsd/cfbam-structured.xsd";
	public final static String	SCHEMA_ROOT_URI = "http://mcf.markhome.server/mcf/v3_1/xsd/cfbam-structured.xsd";
	public final static String CFSEC_XMLNS = "http://mcf.markhome.server/mcf/v3_1/xsd/cfsec-structured.xsd";
	public final static String CFSEC_URI = "http://mcf.markhome.server/mcf/v3_1/xsd/cfsec-structured.xsd";
	public final static String CFSEC_ROOT_URI = "http://mcf.markhome.server/mcf/v3_1/xsd/cfsec-structured.xsd";

	public final static String CFINT_XMLNS = "http://mcf.markhome.server/mcf/v3_1/xsd/cfint-structured.xsd";
	public final static String CFINT_URI = "http://mcf.markhome.server/mcf/v3_1/xsd/cfint-structured.xsd";
	public final static String CFINT_ROOT_URI = "http://mcf.markhome.server/mcf/v3_1/xsd/cfint-structured.xsd";


	// The schema instance to load in to

	private ICFBamSchemaObj schemaObj = null;

	// The cluster to use for loading

	private ICFSecClusterObj useCluster = null;

	public ICFSecClusterObj getUseCluster() {
		return( useCluster );
	}

	public void setUseCluster( ICFSecClusterObj value ) {
		useCluster = value;
	}

	// The tenant to use for loading

	private ICFSecTenantObj useTenant = null;

	public ICFSecTenantObj getUseTenant() {
		return( useTenant );
	}

	public void setUseTenant( ICFSecTenantObj value ) {
		useTenant = value;
	}

	// Loader behaviour configuration attributes

	public enum LoaderBehaviourEnum {
		Insert,
		Update,
		Replace
	};
	private LoaderBehaviourEnum atomLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum blobColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum blobDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum blobTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum boolColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum boolDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum boolTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum chainLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum clearDepLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum clearSubDep1LoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum clearSubDep2LoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum clearSubDep3LoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum clearTopDepLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum clusterLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum dateColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dateDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dateTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash128ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash128DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash128GenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash128TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash160ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash160DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash160GenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash160TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash224ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash224DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash224GenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash224TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash256ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash256DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash256GenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash256TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash384ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash384DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash384GenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash384TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash512ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash512DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash512GenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum dbKeyHash512TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum delDepLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum delSubDep1LoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum delSubDep2LoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum delSubDep3LoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum delTopDepLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum doubleColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum doubleDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum doubleTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum enumDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum enumTagLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum enumTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum floatColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum floatDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum floatTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum iSOCcyLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum iSOCtryLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum iSOCtryCcyLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum iSOCtryLangLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum iSOLangLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum iSOTZoneLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum id16GenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum id32GenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum id64GenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum indexLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum indexColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum int16ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum int16DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum int16TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum int32ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum int32DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum int32TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum int64ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum int64DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum int64TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum licenseLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum majorVersionLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum mimeTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum minorVersionLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum nmTokenColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum nmTokenDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum nmTokenTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum nmTokensColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum nmTokensDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum nmTokensTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum numberColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum numberDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum numberTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum paramLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum popDepLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum popSubDep1LoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum popSubDep2LoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum popSubDep3LoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum popTopDepLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum relationLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum relationColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum schemaDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum schemaRefLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum scopeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum secClusGrpLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secClusGrpIncLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secClusGrpMembLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secSessionLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secSysGrpLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secSysGrpIncLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secSysGrpMembLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secTentGrpLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secTentGrpIncLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secTentGrpMembLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secUserLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secUserPWHistoryLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secUserPasswordLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum serverListFuncLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum serverMethodLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum serverObjFuncLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum serverProcLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum stringColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum stringDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum stringTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum subProjectLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum sysClusterLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum tZDateColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tZDateDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tZDateTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tZTimeColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tZTimeDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tZTimeTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tZTimestampColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tZTimestampDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tZTimestampTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tableLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tableColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tenantLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum textColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum textDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum textTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum timeColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum timeDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum timeTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum timestampColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum timestampDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum timestampTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tldLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tokenColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tokenDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum tokenTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum topDomainLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum topProjectLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uInt16ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uInt16DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uInt16TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uInt32ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uInt32DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uInt32TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uInt64ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uInt64DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uInt64TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uRLProtocolLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uuid6ColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uuid6DefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uuid6GenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uuid6TypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uuidColLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uuidDefLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uuidGenLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uuidTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum valueLoaderBehaviour = LoaderBehaviourEnum.Update;


	// Constructors

	public CFBamSaxLoader() {
		super();
		setRootElementHandler( getSaxRootHandler() );
		initParser();
	}

	public CFBamSaxLoader( ICFLibMessageLog logger ) {
		super( logger );
		setRootElementHandler( getSaxRootHandler() );
		initParser();
	}

	// Element Handler instances

	private CFBamSaxLoaderAtom atomHandler = null;
	private CFBamSaxLoaderBlobCol blobColHandler = null;
	private CFBamSaxLoaderBlobDef blobDefHandler = null;
	private CFBamSaxLoaderBlobType blobTypeHandler = null;
	private CFBamSaxLoaderBoolCol boolColHandler = null;
	private CFBamSaxLoaderBoolDef boolDefHandler = null;
	private CFBamSaxLoaderBoolType boolTypeHandler = null;
	private CFBamSaxLoaderChain chainHandler = null;
	private CFBamSaxLoaderClearDep clearDepHandler = null;
	private CFBamSaxLoaderClearSubDep1 clearSubDep1Handler = null;
	private CFBamSaxLoaderClearSubDep2 clearSubDep2Handler = null;
	private CFBamSaxLoaderClearSubDep3 clearSubDep3Handler = null;
	private CFBamSaxLoaderClearTopDep clearTopDepHandler = null;
	private CFBamSaxLoaderCluster clusterHandler = null;
	private CFBamSaxLoaderDateCol dateColHandler = null;
	private CFBamSaxLoaderDateDef dateDefHandler = null;
	private CFBamSaxLoaderDateType dateTypeHandler = null;
	private CFBamSaxLoaderDbKeyHash128Col dbKeyHash128ColHandler = null;
	private CFBamSaxLoaderDbKeyHash128Def dbKeyHash128DefHandler = null;
	private CFBamSaxLoaderDbKeyHash128Gen dbKeyHash128GenHandler = null;
	private CFBamSaxLoaderDbKeyHash128Type dbKeyHash128TypeHandler = null;
	private CFBamSaxLoaderDbKeyHash160Col dbKeyHash160ColHandler = null;
	private CFBamSaxLoaderDbKeyHash160Def dbKeyHash160DefHandler = null;
	private CFBamSaxLoaderDbKeyHash160Gen dbKeyHash160GenHandler = null;
	private CFBamSaxLoaderDbKeyHash160Type dbKeyHash160TypeHandler = null;
	private CFBamSaxLoaderDbKeyHash224Col dbKeyHash224ColHandler = null;
	private CFBamSaxLoaderDbKeyHash224Def dbKeyHash224DefHandler = null;
	private CFBamSaxLoaderDbKeyHash224Gen dbKeyHash224GenHandler = null;
	private CFBamSaxLoaderDbKeyHash224Type dbKeyHash224TypeHandler = null;
	private CFBamSaxLoaderDbKeyHash256Col dbKeyHash256ColHandler = null;
	private CFBamSaxLoaderDbKeyHash256Def dbKeyHash256DefHandler = null;
	private CFBamSaxLoaderDbKeyHash256Gen dbKeyHash256GenHandler = null;
	private CFBamSaxLoaderDbKeyHash256Type dbKeyHash256TypeHandler = null;
	private CFBamSaxLoaderDbKeyHash384Col dbKeyHash384ColHandler = null;
	private CFBamSaxLoaderDbKeyHash384Def dbKeyHash384DefHandler = null;
	private CFBamSaxLoaderDbKeyHash384Gen dbKeyHash384GenHandler = null;
	private CFBamSaxLoaderDbKeyHash384Type dbKeyHash384TypeHandler = null;
	private CFBamSaxLoaderDbKeyHash512Col dbKeyHash512ColHandler = null;
	private CFBamSaxLoaderDbKeyHash512Def dbKeyHash512DefHandler = null;
	private CFBamSaxLoaderDbKeyHash512Gen dbKeyHash512GenHandler = null;
	private CFBamSaxLoaderDbKeyHash512Type dbKeyHash512TypeHandler = null;
	private CFBamSaxLoaderDelDep delDepHandler = null;
	private CFBamSaxLoaderDelSubDep1 delSubDep1Handler = null;
	private CFBamSaxLoaderDelSubDep2 delSubDep2Handler = null;
	private CFBamSaxLoaderDelSubDep3 delSubDep3Handler = null;
	private CFBamSaxLoaderDelTopDep delTopDepHandler = null;
	private CFBamSaxLoaderDoubleCol doubleColHandler = null;
	private CFBamSaxLoaderDoubleDef doubleDefHandler = null;
	private CFBamSaxLoaderDoubleType doubleTypeHandler = null;
	private CFBamSaxLoaderEnumDef enumDefHandler = null;
	private CFBamSaxLoaderEnumTag enumTagHandler = null;
	private CFBamSaxLoaderEnumType enumTypeHandler = null;
	private CFBamSaxLoaderFloatCol floatColHandler = null;
	private CFBamSaxLoaderFloatDef floatDefHandler = null;
	private CFBamSaxLoaderFloatType floatTypeHandler = null;
	private CFBamSaxLoaderISOCcy iSOCcyHandler = null;
	private CFBamSaxLoaderISOCtry iSOCtryHandler = null;
	private CFBamSaxLoaderISOCtryCcy iSOCtryCcyHandler = null;
	private CFBamSaxLoaderISOCtryLang iSOCtryLangHandler = null;
	private CFBamSaxLoaderISOLang iSOLangHandler = null;
	private CFBamSaxLoaderISOTZone iSOTZoneHandler = null;
	private CFBamSaxLoaderId16Gen id16GenHandler = null;
	private CFBamSaxLoaderId32Gen id32GenHandler = null;
	private CFBamSaxLoaderId64Gen id64GenHandler = null;
	private CFBamSaxLoaderIndex indexHandler = null;
	private CFBamSaxLoaderIndexCol indexColHandler = null;
	private CFBamSaxLoaderInt16Col int16ColHandler = null;
	private CFBamSaxLoaderInt16Def int16DefHandler = null;
	private CFBamSaxLoaderInt16Type int16TypeHandler = null;
	private CFBamSaxLoaderInt32Col int32ColHandler = null;
	private CFBamSaxLoaderInt32Def int32DefHandler = null;
	private CFBamSaxLoaderInt32Type int32TypeHandler = null;
	private CFBamSaxLoaderInt64Col int64ColHandler = null;
	private CFBamSaxLoaderInt64Def int64DefHandler = null;
	private CFBamSaxLoaderInt64Type int64TypeHandler = null;
	private CFBamSaxLoaderLicense licenseHandler = null;
	private CFBamSaxLoaderMajorVersion majorVersionHandler = null;
	private CFBamSaxLoaderMimeType mimeTypeHandler = null;
	private CFBamSaxLoaderMinorVersion minorVersionHandler = null;
	private CFBamSaxLoaderNmTokenCol nmTokenColHandler = null;
	private CFBamSaxLoaderNmTokenDef nmTokenDefHandler = null;
	private CFBamSaxLoaderNmTokenType nmTokenTypeHandler = null;
	private CFBamSaxLoaderNmTokensCol nmTokensColHandler = null;
	private CFBamSaxLoaderNmTokensDef nmTokensDefHandler = null;
	private CFBamSaxLoaderNmTokensType nmTokensTypeHandler = null;
	private CFBamSaxLoaderNumberCol numberColHandler = null;
	private CFBamSaxLoaderNumberDef numberDefHandler = null;
	private CFBamSaxLoaderNumberType numberTypeHandler = null;
	private CFBamSaxLoaderParam paramHandler = null;
	private CFBamSaxLoaderPopDep popDepHandler = null;
	private CFBamSaxLoaderPopSubDep1 popSubDep1Handler = null;
	private CFBamSaxLoaderPopSubDep2 popSubDep2Handler = null;
	private CFBamSaxLoaderPopSubDep3 popSubDep3Handler = null;
	private CFBamSaxLoaderPopTopDep popTopDepHandler = null;
	private CFBamSaxLoaderRelation relationHandler = null;
	private CFBamSaxLoaderRelationCol relationColHandler = null;
	private CFBamSaxLoaderSchemaDef schemaDefHandler = null;
	private CFBamSaxLoaderSchemaRef schemaRefHandler = null;
	private CFBamSaxLoaderScope scopeHandler = null;
	private CFBamSaxLoaderSecClusGrp secClusGrpHandler = null;
	private CFBamSaxLoaderSecClusGrpInc secClusGrpIncHandler = null;
	private CFBamSaxLoaderSecClusGrpMemb secClusGrpMembHandler = null;
	private CFBamSaxLoaderSecSession secSessionHandler = null;
	private CFBamSaxLoaderSecSysGrp secSysGrpHandler = null;
	private CFBamSaxLoaderSecSysGrpInc secSysGrpIncHandler = null;
	private CFBamSaxLoaderSecSysGrpMemb secSysGrpMembHandler = null;
	private CFBamSaxLoaderSecTentGrp secTentGrpHandler = null;
	private CFBamSaxLoaderSecTentGrpInc secTentGrpIncHandler = null;
	private CFBamSaxLoaderSecTentGrpMemb secTentGrpMembHandler = null;
	private CFBamSaxLoaderSecUser secUserHandler = null;
	private CFBamSaxLoaderSecUserPWHistory secUserPWHistoryHandler = null;
	private CFBamSaxLoaderSecUserPassword secUserPasswordHandler = null;
	private CFBamSaxLoaderServerListFunc serverListFuncHandler = null;
	private CFBamSaxLoaderServerMethod serverMethodHandler = null;
	private CFBamSaxLoaderServerObjFunc serverObjFuncHandler = null;
	private CFBamSaxLoaderServerProc serverProcHandler = null;
	private CFBamSaxLoaderStringCol stringColHandler = null;
	private CFBamSaxLoaderStringDef stringDefHandler = null;
	private CFBamSaxLoaderStringType stringTypeHandler = null;
	private CFBamSaxLoaderSubProject subProjectHandler = null;
	private CFBamSaxLoaderSysCluster sysClusterHandler = null;
	private CFBamSaxLoaderTZDateCol tZDateColHandler = null;
	private CFBamSaxLoaderTZDateDef tZDateDefHandler = null;
	private CFBamSaxLoaderTZDateType tZDateTypeHandler = null;
	private CFBamSaxLoaderTZTimeCol tZTimeColHandler = null;
	private CFBamSaxLoaderTZTimeDef tZTimeDefHandler = null;
	private CFBamSaxLoaderTZTimeType tZTimeTypeHandler = null;
	private CFBamSaxLoaderTZTimestampCol tZTimestampColHandler = null;
	private CFBamSaxLoaderTZTimestampDef tZTimestampDefHandler = null;
	private CFBamSaxLoaderTZTimestampType tZTimestampTypeHandler = null;
	private CFBamSaxLoaderTable tableHandler = null;
	private CFBamSaxLoaderTableCol tableColHandler = null;
	private CFBamSaxLoaderTenant tenantHandler = null;
	private CFBamSaxLoaderTextCol textColHandler = null;
	private CFBamSaxLoaderTextDef textDefHandler = null;
	private CFBamSaxLoaderTextType textTypeHandler = null;
	private CFBamSaxLoaderTimeCol timeColHandler = null;
	private CFBamSaxLoaderTimeDef timeDefHandler = null;
	private CFBamSaxLoaderTimeType timeTypeHandler = null;
	private CFBamSaxLoaderTimestampCol timestampColHandler = null;
	private CFBamSaxLoaderTimestampDef timestampDefHandler = null;
	private CFBamSaxLoaderTimestampType timestampTypeHandler = null;
	private CFBamSaxLoaderTld tldHandler = null;
	private CFBamSaxLoaderTokenCol tokenColHandler = null;
	private CFBamSaxLoaderTokenDef tokenDefHandler = null;
	private CFBamSaxLoaderTokenType tokenTypeHandler = null;
	private CFBamSaxLoaderTopDomain topDomainHandler = null;
	private CFBamSaxLoaderTopProject topProjectHandler = null;
	private CFBamSaxLoaderUInt16Col uInt16ColHandler = null;
	private CFBamSaxLoaderUInt16Def uInt16DefHandler = null;
	private CFBamSaxLoaderUInt16Type uInt16TypeHandler = null;
	private CFBamSaxLoaderUInt32Col uInt32ColHandler = null;
	private CFBamSaxLoaderUInt32Def uInt32DefHandler = null;
	private CFBamSaxLoaderUInt32Type uInt32TypeHandler = null;
	private CFBamSaxLoaderUInt64Col uInt64ColHandler = null;
	private CFBamSaxLoaderUInt64Def uInt64DefHandler = null;
	private CFBamSaxLoaderUInt64Type uInt64TypeHandler = null;
	private CFBamSaxLoaderURLProtocol uRLProtocolHandler = null;
	private CFBamSaxLoaderUuid6Col uuid6ColHandler = null;
	private CFBamSaxLoaderUuid6Def uuid6DefHandler = null;
	private CFBamSaxLoaderUuid6Gen uuid6GenHandler = null;
	private CFBamSaxLoaderUuid6Type uuid6TypeHandler = null;
	private CFBamSaxLoaderUuidCol uuidColHandler = null;
	private CFBamSaxLoaderUuidDef uuidDefHandler = null;
	private CFBamSaxLoaderUuidGen uuidGenHandler = null;
	private CFBamSaxLoaderUuidType uuidTypeHandler = null;
	private CFBamSaxLoaderValue valueHandler = null;
	private CFBamSaxRootHandler saxRootHandler = null;

	private CFBamSaxDocHandler saxDocHandler = null;

	// Schema object accessors

	// SchemaObj accessors

	public ICFBamSchemaObj getSchemaObj() {
		return( schemaObj );
	}

	public void setSchemaObj( ICFBamSchemaObj value ) {
		schemaObj = value;
	}

	// Element Handler Resolver Factories

	protected CFBamSaxLoaderAtom getAtomHandler() {
		if( atomHandler == null ) {
			atomHandler = new CFBamSaxLoaderAtom( this );
		}
		return( atomHandler );
	}
	protected CFBamSaxLoaderBlobCol getBlobColHandler() {
		if( blobColHandler == null ) {
			blobColHandler = new CFBamSaxLoaderBlobCol( this );
		}
		return( blobColHandler );
	}
	protected CFBamSaxLoaderBlobDef getBlobDefHandler() {
		if( blobDefHandler == null ) {
			blobDefHandler = new CFBamSaxLoaderBlobDef( this );
		}
		return( blobDefHandler );
	}
	protected CFBamSaxLoaderBlobType getBlobTypeHandler() {
		if( blobTypeHandler == null ) {
			blobTypeHandler = new CFBamSaxLoaderBlobType( this );
		}
		return( blobTypeHandler );
	}
	protected CFBamSaxLoaderBoolCol getBoolColHandler() {
		if( boolColHandler == null ) {
			boolColHandler = new CFBamSaxLoaderBoolCol( this );
		}
		return( boolColHandler );
	}
	protected CFBamSaxLoaderBoolDef getBoolDefHandler() {
		if( boolDefHandler == null ) {
			boolDefHandler = new CFBamSaxLoaderBoolDef( this );
		}
		return( boolDefHandler );
	}
	protected CFBamSaxLoaderBoolType getBoolTypeHandler() {
		if( boolTypeHandler == null ) {
			boolTypeHandler = new CFBamSaxLoaderBoolType( this );
		}
		return( boolTypeHandler );
	}
	protected CFBamSaxLoaderChain getChainHandler() {
		if( chainHandler == null ) {
			chainHandler = new CFBamSaxLoaderChain( this );
		}
		return( chainHandler );
	}
	protected CFBamSaxLoaderClearDep getClearDepHandler() {
		if( clearDepHandler == null ) {
			clearDepHandler = new CFBamSaxLoaderClearDep( this );
		}
		return( clearDepHandler );
	}
	protected CFBamSaxLoaderClearSubDep1 getClearSubDep1Handler() {
		if( clearSubDep1Handler == null ) {
			clearSubDep1Handler = new CFBamSaxLoaderClearSubDep1( this );
			clearSubDep1Handler.addElementHandler( "ClearSubDep2", getClearSubDep2Handler() );
		}
		return( clearSubDep1Handler );
	}
	protected CFBamSaxLoaderClearSubDep2 getClearSubDep2Handler() {
		if( clearSubDep2Handler == null ) {
			clearSubDep2Handler = new CFBamSaxLoaderClearSubDep2( this );
			clearSubDep2Handler.addElementHandler( "ClearSubDep3", getClearSubDep3Handler() );
		}
		return( clearSubDep2Handler );
	}
	protected CFBamSaxLoaderClearSubDep3 getClearSubDep3Handler() {
		if( clearSubDep3Handler == null ) {
			clearSubDep3Handler = new CFBamSaxLoaderClearSubDep3( this );
		}
		return( clearSubDep3Handler );
	}
	protected CFBamSaxLoaderClearTopDep getClearTopDepHandler() {
		if( clearTopDepHandler == null ) {
			clearTopDepHandler = new CFBamSaxLoaderClearTopDep( this );
			clearTopDepHandler.addElementHandler( "ClearSubDep1", getClearSubDep1Handler() );
		}
		return( clearTopDepHandler );
	}
	protected CFBamSaxLoaderCluster getClusterHandler() {
		if( clusterHandler == null ) {
			clusterHandler = new CFBamSaxLoaderCluster( this );
			clusterHandler.addElementHandler( "Tenant", getTenantHandler() );
			clusterHandler.addElementHandler( "SysCluster", getSysClusterHandler() );
		}
		return( clusterHandler );
	}
	protected CFBamSaxLoaderDateCol getDateColHandler() {
		if( dateColHandler == null ) {
			dateColHandler = new CFBamSaxLoaderDateCol( this );
		}
		return( dateColHandler );
	}
	protected CFBamSaxLoaderDateDef getDateDefHandler() {
		if( dateDefHandler == null ) {
			dateDefHandler = new CFBamSaxLoaderDateDef( this );
		}
		return( dateDefHandler );
	}
	protected CFBamSaxLoaderDateType getDateTypeHandler() {
		if( dateTypeHandler == null ) {
			dateTypeHandler = new CFBamSaxLoaderDateType( this );
		}
		return( dateTypeHandler );
	}
	protected CFBamSaxLoaderDbKeyHash128Col getDbKeyHash128ColHandler() {
		if( dbKeyHash128ColHandler == null ) {
			dbKeyHash128ColHandler = new CFBamSaxLoaderDbKeyHash128Col( this );
		}
		return( dbKeyHash128ColHandler );
	}
	protected CFBamSaxLoaderDbKeyHash128Def getDbKeyHash128DefHandler() {
		if( dbKeyHash128DefHandler == null ) {
			dbKeyHash128DefHandler = new CFBamSaxLoaderDbKeyHash128Def( this );
		}
		return( dbKeyHash128DefHandler );
	}
	protected CFBamSaxLoaderDbKeyHash128Gen getDbKeyHash128GenHandler() {
		if( dbKeyHash128GenHandler == null ) {
			dbKeyHash128GenHandler = new CFBamSaxLoaderDbKeyHash128Gen( this );
		}
		return( dbKeyHash128GenHandler );
	}
	protected CFBamSaxLoaderDbKeyHash128Type getDbKeyHash128TypeHandler() {
		if( dbKeyHash128TypeHandler == null ) {
			dbKeyHash128TypeHandler = new CFBamSaxLoaderDbKeyHash128Type( this );
		}
		return( dbKeyHash128TypeHandler );
	}
	protected CFBamSaxLoaderDbKeyHash160Col getDbKeyHash160ColHandler() {
		if( dbKeyHash160ColHandler == null ) {
			dbKeyHash160ColHandler = new CFBamSaxLoaderDbKeyHash160Col( this );
		}
		return( dbKeyHash160ColHandler );
	}
	protected CFBamSaxLoaderDbKeyHash160Def getDbKeyHash160DefHandler() {
		if( dbKeyHash160DefHandler == null ) {
			dbKeyHash160DefHandler = new CFBamSaxLoaderDbKeyHash160Def( this );
		}
		return( dbKeyHash160DefHandler );
	}
	protected CFBamSaxLoaderDbKeyHash160Gen getDbKeyHash160GenHandler() {
		if( dbKeyHash160GenHandler == null ) {
			dbKeyHash160GenHandler = new CFBamSaxLoaderDbKeyHash160Gen( this );
		}
		return( dbKeyHash160GenHandler );
	}
	protected CFBamSaxLoaderDbKeyHash160Type getDbKeyHash160TypeHandler() {
		if( dbKeyHash160TypeHandler == null ) {
			dbKeyHash160TypeHandler = new CFBamSaxLoaderDbKeyHash160Type( this );
		}
		return( dbKeyHash160TypeHandler );
	}
	protected CFBamSaxLoaderDbKeyHash224Col getDbKeyHash224ColHandler() {
		if( dbKeyHash224ColHandler == null ) {
			dbKeyHash224ColHandler = new CFBamSaxLoaderDbKeyHash224Col( this );
		}
		return( dbKeyHash224ColHandler );
	}
	protected CFBamSaxLoaderDbKeyHash224Def getDbKeyHash224DefHandler() {
		if( dbKeyHash224DefHandler == null ) {
			dbKeyHash224DefHandler = new CFBamSaxLoaderDbKeyHash224Def( this );
		}
		return( dbKeyHash224DefHandler );
	}
	protected CFBamSaxLoaderDbKeyHash224Gen getDbKeyHash224GenHandler() {
		if( dbKeyHash224GenHandler == null ) {
			dbKeyHash224GenHandler = new CFBamSaxLoaderDbKeyHash224Gen( this );
		}
		return( dbKeyHash224GenHandler );
	}
	protected CFBamSaxLoaderDbKeyHash224Type getDbKeyHash224TypeHandler() {
		if( dbKeyHash224TypeHandler == null ) {
			dbKeyHash224TypeHandler = new CFBamSaxLoaderDbKeyHash224Type( this );
		}
		return( dbKeyHash224TypeHandler );
	}
	protected CFBamSaxLoaderDbKeyHash256Col getDbKeyHash256ColHandler() {
		if( dbKeyHash256ColHandler == null ) {
			dbKeyHash256ColHandler = new CFBamSaxLoaderDbKeyHash256Col( this );
		}
		return( dbKeyHash256ColHandler );
	}
	protected CFBamSaxLoaderDbKeyHash256Def getDbKeyHash256DefHandler() {
		if( dbKeyHash256DefHandler == null ) {
			dbKeyHash256DefHandler = new CFBamSaxLoaderDbKeyHash256Def( this );
		}
		return( dbKeyHash256DefHandler );
	}
	protected CFBamSaxLoaderDbKeyHash256Gen getDbKeyHash256GenHandler() {
		if( dbKeyHash256GenHandler == null ) {
			dbKeyHash256GenHandler = new CFBamSaxLoaderDbKeyHash256Gen( this );
		}
		return( dbKeyHash256GenHandler );
	}
	protected CFBamSaxLoaderDbKeyHash256Type getDbKeyHash256TypeHandler() {
		if( dbKeyHash256TypeHandler == null ) {
			dbKeyHash256TypeHandler = new CFBamSaxLoaderDbKeyHash256Type( this );
		}
		return( dbKeyHash256TypeHandler );
	}
	protected CFBamSaxLoaderDbKeyHash384Col getDbKeyHash384ColHandler() {
		if( dbKeyHash384ColHandler == null ) {
			dbKeyHash384ColHandler = new CFBamSaxLoaderDbKeyHash384Col( this );
		}
		return( dbKeyHash384ColHandler );
	}
	protected CFBamSaxLoaderDbKeyHash384Def getDbKeyHash384DefHandler() {
		if( dbKeyHash384DefHandler == null ) {
			dbKeyHash384DefHandler = new CFBamSaxLoaderDbKeyHash384Def( this );
		}
		return( dbKeyHash384DefHandler );
	}
	protected CFBamSaxLoaderDbKeyHash384Gen getDbKeyHash384GenHandler() {
		if( dbKeyHash384GenHandler == null ) {
			dbKeyHash384GenHandler = new CFBamSaxLoaderDbKeyHash384Gen( this );
		}
		return( dbKeyHash384GenHandler );
	}
	protected CFBamSaxLoaderDbKeyHash384Type getDbKeyHash384TypeHandler() {
		if( dbKeyHash384TypeHandler == null ) {
			dbKeyHash384TypeHandler = new CFBamSaxLoaderDbKeyHash384Type( this );
		}
		return( dbKeyHash384TypeHandler );
	}
	protected CFBamSaxLoaderDbKeyHash512Col getDbKeyHash512ColHandler() {
		if( dbKeyHash512ColHandler == null ) {
			dbKeyHash512ColHandler = new CFBamSaxLoaderDbKeyHash512Col( this );
		}
		return( dbKeyHash512ColHandler );
	}
	protected CFBamSaxLoaderDbKeyHash512Def getDbKeyHash512DefHandler() {
		if( dbKeyHash512DefHandler == null ) {
			dbKeyHash512DefHandler = new CFBamSaxLoaderDbKeyHash512Def( this );
		}
		return( dbKeyHash512DefHandler );
	}
	protected CFBamSaxLoaderDbKeyHash512Gen getDbKeyHash512GenHandler() {
		if( dbKeyHash512GenHandler == null ) {
			dbKeyHash512GenHandler = new CFBamSaxLoaderDbKeyHash512Gen( this );
		}
		return( dbKeyHash512GenHandler );
	}
	protected CFBamSaxLoaderDbKeyHash512Type getDbKeyHash512TypeHandler() {
		if( dbKeyHash512TypeHandler == null ) {
			dbKeyHash512TypeHandler = new CFBamSaxLoaderDbKeyHash512Type( this );
		}
		return( dbKeyHash512TypeHandler );
	}
	protected CFBamSaxLoaderDelDep getDelDepHandler() {
		if( delDepHandler == null ) {
			delDepHandler = new CFBamSaxLoaderDelDep( this );
		}
		return( delDepHandler );
	}
	protected CFBamSaxLoaderDelSubDep1 getDelSubDep1Handler() {
		if( delSubDep1Handler == null ) {
			delSubDep1Handler = new CFBamSaxLoaderDelSubDep1( this );
			delSubDep1Handler.addElementHandler( "DelSubDep2", getDelSubDep2Handler() );
		}
		return( delSubDep1Handler );
	}
	protected CFBamSaxLoaderDelSubDep2 getDelSubDep2Handler() {
		if( delSubDep2Handler == null ) {
			delSubDep2Handler = new CFBamSaxLoaderDelSubDep2( this );
			delSubDep2Handler.addElementHandler( "DelSubDep3", getDelSubDep3Handler() );
		}
		return( delSubDep2Handler );
	}
	protected CFBamSaxLoaderDelSubDep3 getDelSubDep3Handler() {
		if( delSubDep3Handler == null ) {
			delSubDep3Handler = new CFBamSaxLoaderDelSubDep3( this );
		}
		return( delSubDep3Handler );
	}
	protected CFBamSaxLoaderDelTopDep getDelTopDepHandler() {
		if( delTopDepHandler == null ) {
			delTopDepHandler = new CFBamSaxLoaderDelTopDep( this );
			delTopDepHandler.addElementHandler( "DelSubDep1", getDelSubDep1Handler() );
		}
		return( delTopDepHandler );
	}
	protected CFBamSaxLoaderDoubleCol getDoubleColHandler() {
		if( doubleColHandler == null ) {
			doubleColHandler = new CFBamSaxLoaderDoubleCol( this );
		}
		return( doubleColHandler );
	}
	protected CFBamSaxLoaderDoubleDef getDoubleDefHandler() {
		if( doubleDefHandler == null ) {
			doubleDefHandler = new CFBamSaxLoaderDoubleDef( this );
		}
		return( doubleDefHandler );
	}
	protected CFBamSaxLoaderDoubleType getDoubleTypeHandler() {
		if( doubleTypeHandler == null ) {
			doubleTypeHandler = new CFBamSaxLoaderDoubleType( this );
		}
		return( doubleTypeHandler );
	}
	protected CFBamSaxLoaderEnumDef getEnumDefHandler() {
		if( enumDefHandler == null ) {
			enumDefHandler = new CFBamSaxLoaderEnumDef( this );
			enumDefHandler.addElementHandler( "EnumTag", getEnumTagHandler() );
		}
		return( enumDefHandler );
	}
	protected CFBamSaxLoaderEnumTag getEnumTagHandler() {
		if( enumTagHandler == null ) {
			enumTagHandler = new CFBamSaxLoaderEnumTag( this );
		}
		return( enumTagHandler );
	}
	protected CFBamSaxLoaderEnumType getEnumTypeHandler() {
		if( enumTypeHandler == null ) {
			enumTypeHandler = new CFBamSaxLoaderEnumType( this );
			enumTypeHandler.addElementHandler( "EnumTag", getEnumTagHandler() );
		}
		return( enumTypeHandler );
	}
	protected CFBamSaxLoaderFloatCol getFloatColHandler() {
		if( floatColHandler == null ) {
			floatColHandler = new CFBamSaxLoaderFloatCol( this );
		}
		return( floatColHandler );
	}
	protected CFBamSaxLoaderFloatDef getFloatDefHandler() {
		if( floatDefHandler == null ) {
			floatDefHandler = new CFBamSaxLoaderFloatDef( this );
		}
		return( floatDefHandler );
	}
	protected CFBamSaxLoaderFloatType getFloatTypeHandler() {
		if( floatTypeHandler == null ) {
			floatTypeHandler = new CFBamSaxLoaderFloatType( this );
		}
		return( floatTypeHandler );
	}
	protected CFBamSaxLoaderISOCcy getISOCcyHandler() {
		if( iSOCcyHandler == null ) {
			iSOCcyHandler = new CFBamSaxLoaderISOCcy( this );
		}
		return( iSOCcyHandler );
	}
	protected CFBamSaxLoaderISOCtry getISOCtryHandler() {
		if( iSOCtryHandler == null ) {
			iSOCtryHandler = new CFBamSaxLoaderISOCtry( this );
			iSOCtryHandler.addElementHandler( "ISOCtryCcy", getISOCtryCcyHandler() );
			iSOCtryHandler.addElementHandler( "ISOCtryLang", getISOCtryLangHandler() );
		}
		return( iSOCtryHandler );
	}
	protected CFBamSaxLoaderISOCtryCcy getISOCtryCcyHandler() {
		if( iSOCtryCcyHandler == null ) {
			iSOCtryCcyHandler = new CFBamSaxLoaderISOCtryCcy( this );
		}
		return( iSOCtryCcyHandler );
	}
	protected CFBamSaxLoaderISOCtryLang getISOCtryLangHandler() {
		if( iSOCtryLangHandler == null ) {
			iSOCtryLangHandler = new CFBamSaxLoaderISOCtryLang( this );
		}
		return( iSOCtryLangHandler );
	}
	protected CFBamSaxLoaderISOLang getISOLangHandler() {
		if( iSOLangHandler == null ) {
			iSOLangHandler = new CFBamSaxLoaderISOLang( this );
		}
		return( iSOLangHandler );
	}
	protected CFBamSaxLoaderISOTZone getISOTZoneHandler() {
		if( iSOTZoneHandler == null ) {
			iSOTZoneHandler = new CFBamSaxLoaderISOTZone( this );
		}
		return( iSOTZoneHandler );
	}
	protected CFBamSaxLoaderId16Gen getId16GenHandler() {
		if( id16GenHandler == null ) {
			id16GenHandler = new CFBamSaxLoaderId16Gen( this );
		}
		return( id16GenHandler );
	}
	protected CFBamSaxLoaderId32Gen getId32GenHandler() {
		if( id32GenHandler == null ) {
			id32GenHandler = new CFBamSaxLoaderId32Gen( this );
		}
		return( id32GenHandler );
	}
	protected CFBamSaxLoaderId64Gen getId64GenHandler() {
		if( id64GenHandler == null ) {
			id64GenHandler = new CFBamSaxLoaderId64Gen( this );
		}
		return( id64GenHandler );
	}
	protected CFBamSaxLoaderIndex getIndexHandler() {
		if( indexHandler == null ) {
			indexHandler = new CFBamSaxLoaderIndex( this );
			indexHandler.addElementHandler( "IndexCol", getIndexColHandler() );
		}
		return( indexHandler );
	}
	protected CFBamSaxLoaderIndexCol getIndexColHandler() {
		if( indexColHandler == null ) {
			indexColHandler = new CFBamSaxLoaderIndexCol( this );
		}
		return( indexColHandler );
	}
	protected CFBamSaxLoaderInt16Col getInt16ColHandler() {
		if( int16ColHandler == null ) {
			int16ColHandler = new CFBamSaxLoaderInt16Col( this );
		}
		return( int16ColHandler );
	}
	protected CFBamSaxLoaderInt16Def getInt16DefHandler() {
		if( int16DefHandler == null ) {
			int16DefHandler = new CFBamSaxLoaderInt16Def( this );
		}
		return( int16DefHandler );
	}
	protected CFBamSaxLoaderInt16Type getInt16TypeHandler() {
		if( int16TypeHandler == null ) {
			int16TypeHandler = new CFBamSaxLoaderInt16Type( this );
		}
		return( int16TypeHandler );
	}
	protected CFBamSaxLoaderInt32Col getInt32ColHandler() {
		if( int32ColHandler == null ) {
			int32ColHandler = new CFBamSaxLoaderInt32Col( this );
		}
		return( int32ColHandler );
	}
	protected CFBamSaxLoaderInt32Def getInt32DefHandler() {
		if( int32DefHandler == null ) {
			int32DefHandler = new CFBamSaxLoaderInt32Def( this );
		}
		return( int32DefHandler );
	}
	protected CFBamSaxLoaderInt32Type getInt32TypeHandler() {
		if( int32TypeHandler == null ) {
			int32TypeHandler = new CFBamSaxLoaderInt32Type( this );
		}
		return( int32TypeHandler );
	}
	protected CFBamSaxLoaderInt64Col getInt64ColHandler() {
		if( int64ColHandler == null ) {
			int64ColHandler = new CFBamSaxLoaderInt64Col( this );
		}
		return( int64ColHandler );
	}
	protected CFBamSaxLoaderInt64Def getInt64DefHandler() {
		if( int64DefHandler == null ) {
			int64DefHandler = new CFBamSaxLoaderInt64Def( this );
		}
		return( int64DefHandler );
	}
	protected CFBamSaxLoaderInt64Type getInt64TypeHandler() {
		if( int64TypeHandler == null ) {
			int64TypeHandler = new CFBamSaxLoaderInt64Type( this );
		}
		return( int64TypeHandler );
	}
	protected CFBamSaxLoaderLicense getLicenseHandler() {
		if( licenseHandler == null ) {
			licenseHandler = new CFBamSaxLoaderLicense( this );
		}
		return( licenseHandler );
	}
	protected CFBamSaxLoaderMajorVersion getMajorVersionHandler() {
		if( majorVersionHandler == null ) {
			majorVersionHandler = new CFBamSaxLoaderMajorVersion( this );
			majorVersionHandler.addElementHandler( "MinorVersion", getMinorVersionHandler() );
		}
		return( majorVersionHandler );
	}
	protected CFBamSaxLoaderMimeType getMimeTypeHandler() {
		if( mimeTypeHandler == null ) {
			mimeTypeHandler = new CFBamSaxLoaderMimeType( this );
		}
		return( mimeTypeHandler );
	}
	protected CFBamSaxLoaderMinorVersion getMinorVersionHandler() {
		if( minorVersionHandler == null ) {
			minorVersionHandler = new CFBamSaxLoaderMinorVersion( this );
			minorVersionHandler.addElementHandler( "SchemaDef", getSchemaDefHandler() );
		}
		return( minorVersionHandler );
	}
	protected CFBamSaxLoaderNmTokenCol getNmTokenColHandler() {
		if( nmTokenColHandler == null ) {
			nmTokenColHandler = new CFBamSaxLoaderNmTokenCol( this );
		}
		return( nmTokenColHandler );
	}
	protected CFBamSaxLoaderNmTokenDef getNmTokenDefHandler() {
		if( nmTokenDefHandler == null ) {
			nmTokenDefHandler = new CFBamSaxLoaderNmTokenDef( this );
		}
		return( nmTokenDefHandler );
	}
	protected CFBamSaxLoaderNmTokenType getNmTokenTypeHandler() {
		if( nmTokenTypeHandler == null ) {
			nmTokenTypeHandler = new CFBamSaxLoaderNmTokenType( this );
		}
		return( nmTokenTypeHandler );
	}
	protected CFBamSaxLoaderNmTokensCol getNmTokensColHandler() {
		if( nmTokensColHandler == null ) {
			nmTokensColHandler = new CFBamSaxLoaderNmTokensCol( this );
		}
		return( nmTokensColHandler );
	}
	protected CFBamSaxLoaderNmTokensDef getNmTokensDefHandler() {
		if( nmTokensDefHandler == null ) {
			nmTokensDefHandler = new CFBamSaxLoaderNmTokensDef( this );
		}
		return( nmTokensDefHandler );
	}
	protected CFBamSaxLoaderNmTokensType getNmTokensTypeHandler() {
		if( nmTokensTypeHandler == null ) {
			nmTokensTypeHandler = new CFBamSaxLoaderNmTokensType( this );
		}
		return( nmTokensTypeHandler );
	}
	protected CFBamSaxLoaderNumberCol getNumberColHandler() {
		if( numberColHandler == null ) {
			numberColHandler = new CFBamSaxLoaderNumberCol( this );
		}
		return( numberColHandler );
	}
	protected CFBamSaxLoaderNumberDef getNumberDefHandler() {
		if( numberDefHandler == null ) {
			numberDefHandler = new CFBamSaxLoaderNumberDef( this );
		}
		return( numberDefHandler );
	}
	protected CFBamSaxLoaderNumberType getNumberTypeHandler() {
		if( numberTypeHandler == null ) {
			numberTypeHandler = new CFBamSaxLoaderNumberType( this );
		}
		return( numberTypeHandler );
	}
	protected CFBamSaxLoaderParam getParamHandler() {
		if( paramHandler == null ) {
			paramHandler = new CFBamSaxLoaderParam( this );
		}
		return( paramHandler );
	}
	protected CFBamSaxLoaderPopDep getPopDepHandler() {
		if( popDepHandler == null ) {
			popDepHandler = new CFBamSaxLoaderPopDep( this );
		}
		return( popDepHandler );
	}
	protected CFBamSaxLoaderPopSubDep1 getPopSubDep1Handler() {
		if( popSubDep1Handler == null ) {
			popSubDep1Handler = new CFBamSaxLoaderPopSubDep1( this );
			popSubDep1Handler.addElementHandler( "PopSubDep2", getPopSubDep2Handler() );
		}
		return( popSubDep1Handler );
	}
	protected CFBamSaxLoaderPopSubDep2 getPopSubDep2Handler() {
		if( popSubDep2Handler == null ) {
			popSubDep2Handler = new CFBamSaxLoaderPopSubDep2( this );
			popSubDep2Handler.addElementHandler( "PopSubDep3", getPopSubDep3Handler() );
		}
		return( popSubDep2Handler );
	}
	protected CFBamSaxLoaderPopSubDep3 getPopSubDep3Handler() {
		if( popSubDep3Handler == null ) {
			popSubDep3Handler = new CFBamSaxLoaderPopSubDep3( this );
		}
		return( popSubDep3Handler );
	}
	protected CFBamSaxLoaderPopTopDep getPopTopDepHandler() {
		if( popTopDepHandler == null ) {
			popTopDepHandler = new CFBamSaxLoaderPopTopDep( this );
			popTopDepHandler.addElementHandler( "PopSubDep1", getPopSubDep1Handler() );
		}
		return( popTopDepHandler );
	}
	protected CFBamSaxLoaderRelation getRelationHandler() {
		if( relationHandler == null ) {
			relationHandler = new CFBamSaxLoaderRelation( this );
			relationHandler.addElementHandler( "RelationCol", getRelationColHandler() );
			relationHandler.addElementHandler( "PopTopDep", getPopTopDepHandler() );
		}
		return( relationHandler );
	}
	protected CFBamSaxLoaderRelationCol getRelationColHandler() {
		if( relationColHandler == null ) {
			relationColHandler = new CFBamSaxLoaderRelationCol( this );
		}
		return( relationColHandler );
	}
	protected CFBamSaxLoaderSchemaDef getSchemaDefHandler() {
		if( schemaDefHandler == null ) {
			schemaDefHandler = new CFBamSaxLoaderSchemaDef( this );
			schemaDefHandler.addElementHandler( "Table", getTableHandler() );
			schemaDefHandler.addElementHandler( "BlobType", getBlobTypeHandler() );
			schemaDefHandler.addElementHandler( "BoolType", getBoolTypeHandler() );
			schemaDefHandler.addElementHandler( "DateType", getDateTypeHandler() );
			schemaDefHandler.addElementHandler( "DoubleType", getDoubleTypeHandler() );
			schemaDefHandler.addElementHandler( "FloatType", getFloatTypeHandler() );
			schemaDefHandler.addElementHandler( "Int16Type", getInt16TypeHandler() );
			schemaDefHandler.addElementHandler( "Id16Gen", getId16GenHandler() );
			schemaDefHandler.addElementHandler( "EnumType", getEnumTypeHandler() );
			schemaDefHandler.addElementHandler( "Int32Type", getInt32TypeHandler() );
			schemaDefHandler.addElementHandler( "Id32Gen", getId32GenHandler() );
			schemaDefHandler.addElementHandler( "Int64Type", getInt64TypeHandler() );
			schemaDefHandler.addElementHandler( "Id64Gen", getId64GenHandler() );
			schemaDefHandler.addElementHandler( "NmTokenType", getNmTokenTypeHandler() );
			schemaDefHandler.addElementHandler( "NmTokensType", getNmTokensTypeHandler() );
			schemaDefHandler.addElementHandler( "NumberType", getNumberTypeHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash128Type", getDbKeyHash128TypeHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash128Gen", getDbKeyHash128GenHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash160Type", getDbKeyHash160TypeHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash160Gen", getDbKeyHash160GenHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash224Type", getDbKeyHash224TypeHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash224Gen", getDbKeyHash224GenHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash256Type", getDbKeyHash256TypeHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash256Gen", getDbKeyHash256GenHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash384Type", getDbKeyHash384TypeHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash384Gen", getDbKeyHash384GenHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash512Type", getDbKeyHash512TypeHandler() );
			schemaDefHandler.addElementHandler( "DbKeyHash512Gen", getDbKeyHash512GenHandler() );
			schemaDefHandler.addElementHandler( "StringType", getStringTypeHandler() );
			schemaDefHandler.addElementHandler( "TZDateType", getTZDateTypeHandler() );
			schemaDefHandler.addElementHandler( "TZTimeType", getTZTimeTypeHandler() );
			schemaDefHandler.addElementHandler( "TZTimestampType", getTZTimestampTypeHandler() );
			schemaDefHandler.addElementHandler( "TextType", getTextTypeHandler() );
			schemaDefHandler.addElementHandler( "TimeType", getTimeTypeHandler() );
			schemaDefHandler.addElementHandler( "TimestampType", getTimestampTypeHandler() );
			schemaDefHandler.addElementHandler( "TokenType", getTokenTypeHandler() );
			schemaDefHandler.addElementHandler( "UInt16Type", getUInt16TypeHandler() );
			schemaDefHandler.addElementHandler( "UInt32Type", getUInt32TypeHandler() );
			schemaDefHandler.addElementHandler( "UInt64Type", getUInt64TypeHandler() );
			schemaDefHandler.addElementHandler( "UuidType", getUuidTypeHandler() );
			schemaDefHandler.addElementHandler( "UuidGen", getUuidGenHandler() );
			schemaDefHandler.addElementHandler( "Uuid6Type", getUuid6TypeHandler() );
			schemaDefHandler.addElementHandler( "Uuid6Gen", getUuid6GenHandler() );
			schemaDefHandler.addElementHandler( "SchemaRef", getSchemaRefHandler() );
		}
		return( schemaDefHandler );
	}
	protected CFBamSaxLoaderSchemaRef getSchemaRefHandler() {
		if( schemaRefHandler == null ) {
			schemaRefHandler = new CFBamSaxLoaderSchemaRef( this );
		}
		return( schemaRefHandler );
	}
	protected CFBamSaxLoaderScope getScopeHandler() {
		if( scopeHandler == null ) {
			scopeHandler = new CFBamSaxLoaderScope( this );
		}
		return( scopeHandler );
	}
	protected CFBamSaxLoaderSecClusGrp getSecClusGrpHandler() {
		if( secClusGrpHandler == null ) {
			secClusGrpHandler = new CFBamSaxLoaderSecClusGrp( this );
		}
		return( secClusGrpHandler );
	}
	protected CFBamSaxLoaderSecClusGrpInc getSecClusGrpIncHandler() {
		if( secClusGrpIncHandler == null ) {
			secClusGrpIncHandler = new CFBamSaxLoaderSecClusGrpInc( this );
		}
		return( secClusGrpIncHandler );
	}
	protected CFBamSaxLoaderSecClusGrpMemb getSecClusGrpMembHandler() {
		if( secClusGrpMembHandler == null ) {
			secClusGrpMembHandler = new CFBamSaxLoaderSecClusGrpMemb( this );
		}
		return( secClusGrpMembHandler );
	}
	protected CFBamSaxLoaderSecSession getSecSessionHandler() {
		if( secSessionHandler == null ) {
			secSessionHandler = new CFBamSaxLoaderSecSession( this );
		}
		return( secSessionHandler );
	}
	protected CFBamSaxLoaderSecSysGrp getSecSysGrpHandler() {
		if( secSysGrpHandler == null ) {
			secSysGrpHandler = new CFBamSaxLoaderSecSysGrp( this );
		}
		return( secSysGrpHandler );
	}
	protected CFBamSaxLoaderSecSysGrpInc getSecSysGrpIncHandler() {
		if( secSysGrpIncHandler == null ) {
			secSysGrpIncHandler = new CFBamSaxLoaderSecSysGrpInc( this );
		}
		return( secSysGrpIncHandler );
	}
	protected CFBamSaxLoaderSecSysGrpMemb getSecSysGrpMembHandler() {
		if( secSysGrpMembHandler == null ) {
			secSysGrpMembHandler = new CFBamSaxLoaderSecSysGrpMemb( this );
		}
		return( secSysGrpMembHandler );
	}
	protected CFBamSaxLoaderSecTentGrp getSecTentGrpHandler() {
		if( secTentGrpHandler == null ) {
			secTentGrpHandler = new CFBamSaxLoaderSecTentGrp( this );
		}
		return( secTentGrpHandler );
	}
	protected CFBamSaxLoaderSecTentGrpInc getSecTentGrpIncHandler() {
		if( secTentGrpIncHandler == null ) {
			secTentGrpIncHandler = new CFBamSaxLoaderSecTentGrpInc( this );
		}
		return( secTentGrpIncHandler );
	}
	protected CFBamSaxLoaderSecTentGrpMemb getSecTentGrpMembHandler() {
		if( secTentGrpMembHandler == null ) {
			secTentGrpMembHandler = new CFBamSaxLoaderSecTentGrpMemb( this );
		}
		return( secTentGrpMembHandler );
	}
	protected CFBamSaxLoaderSecUser getSecUserHandler() {
		if( secUserHandler == null ) {
			secUserHandler = new CFBamSaxLoaderSecUser( this );
		}
		return( secUserHandler );
	}
	protected CFBamSaxLoaderSecUserPWHistory getSecUserPWHistoryHandler() {
		if( secUserPWHistoryHandler == null ) {
			secUserPWHistoryHandler = new CFBamSaxLoaderSecUserPWHistory( this );
		}
		return( secUserPWHistoryHandler );
	}
	protected CFBamSaxLoaderSecUserPassword getSecUserPasswordHandler() {
		if( secUserPasswordHandler == null ) {
			secUserPasswordHandler = new CFBamSaxLoaderSecUserPassword( this );
		}
		return( secUserPasswordHandler );
	}
	protected CFBamSaxLoaderServerListFunc getServerListFuncHandler() {
		if( serverListFuncHandler == null ) {
			serverListFuncHandler = new CFBamSaxLoaderServerListFunc( this );
			serverListFuncHandler.addElementHandler( "Param", getParamHandler() );
		}
		return( serverListFuncHandler );
	}
	protected CFBamSaxLoaderServerMethod getServerMethodHandler() {
		if( serverMethodHandler == null ) {
			serverMethodHandler = new CFBamSaxLoaderServerMethod( this );
			serverMethodHandler.addElementHandler( "Param", getParamHandler() );
		}
		return( serverMethodHandler );
	}
	protected CFBamSaxLoaderServerObjFunc getServerObjFuncHandler() {
		if( serverObjFuncHandler == null ) {
			serverObjFuncHandler = new CFBamSaxLoaderServerObjFunc( this );
			serverObjFuncHandler.addElementHandler( "Param", getParamHandler() );
		}
		return( serverObjFuncHandler );
	}
	protected CFBamSaxLoaderServerProc getServerProcHandler() {
		if( serverProcHandler == null ) {
			serverProcHandler = new CFBamSaxLoaderServerProc( this );
			serverProcHandler.addElementHandler( "Param", getParamHandler() );
		}
		return( serverProcHandler );
	}
	protected CFBamSaxLoaderStringCol getStringColHandler() {
		if( stringColHandler == null ) {
			stringColHandler = new CFBamSaxLoaderStringCol( this );
		}
		return( stringColHandler );
	}
	protected CFBamSaxLoaderStringDef getStringDefHandler() {
		if( stringDefHandler == null ) {
			stringDefHandler = new CFBamSaxLoaderStringDef( this );
		}
		return( stringDefHandler );
	}
	protected CFBamSaxLoaderStringType getStringTypeHandler() {
		if( stringTypeHandler == null ) {
			stringTypeHandler = new CFBamSaxLoaderStringType( this );
		}
		return( stringTypeHandler );
	}
	protected CFBamSaxLoaderSubProject getSubProjectHandler() {
		if( subProjectHandler == null ) {
			subProjectHandler = new CFBamSaxLoaderSubProject( this );
			subProjectHandler.addElementHandler( "MajorVersion", getMajorVersionHandler() );
		}
		return( subProjectHandler );
	}
	protected CFBamSaxLoaderSysCluster getSysClusterHandler() {
		if( sysClusterHandler == null ) {
			sysClusterHandler = new CFBamSaxLoaderSysCluster( this );
		}
		return( sysClusterHandler );
	}
	protected CFBamSaxLoaderTZDateCol getTZDateColHandler() {
		if( tZDateColHandler == null ) {
			tZDateColHandler = new CFBamSaxLoaderTZDateCol( this );
		}
		return( tZDateColHandler );
	}
	protected CFBamSaxLoaderTZDateDef getTZDateDefHandler() {
		if( tZDateDefHandler == null ) {
			tZDateDefHandler = new CFBamSaxLoaderTZDateDef( this );
		}
		return( tZDateDefHandler );
	}
	protected CFBamSaxLoaderTZDateType getTZDateTypeHandler() {
		if( tZDateTypeHandler == null ) {
			tZDateTypeHandler = new CFBamSaxLoaderTZDateType( this );
		}
		return( tZDateTypeHandler );
	}
	protected CFBamSaxLoaderTZTimeCol getTZTimeColHandler() {
		if( tZTimeColHandler == null ) {
			tZTimeColHandler = new CFBamSaxLoaderTZTimeCol( this );
		}
		return( tZTimeColHandler );
	}
	protected CFBamSaxLoaderTZTimeDef getTZTimeDefHandler() {
		if( tZTimeDefHandler == null ) {
			tZTimeDefHandler = new CFBamSaxLoaderTZTimeDef( this );
		}
		return( tZTimeDefHandler );
	}
	protected CFBamSaxLoaderTZTimeType getTZTimeTypeHandler() {
		if( tZTimeTypeHandler == null ) {
			tZTimeTypeHandler = new CFBamSaxLoaderTZTimeType( this );
		}
		return( tZTimeTypeHandler );
	}
	protected CFBamSaxLoaderTZTimestampCol getTZTimestampColHandler() {
		if( tZTimestampColHandler == null ) {
			tZTimestampColHandler = new CFBamSaxLoaderTZTimestampCol( this );
		}
		return( tZTimestampColHandler );
	}
	protected CFBamSaxLoaderTZTimestampDef getTZTimestampDefHandler() {
		if( tZTimestampDefHandler == null ) {
			tZTimestampDefHandler = new CFBamSaxLoaderTZTimestampDef( this );
		}
		return( tZTimestampDefHandler );
	}
	protected CFBamSaxLoaderTZTimestampType getTZTimestampTypeHandler() {
		if( tZTimestampTypeHandler == null ) {
			tZTimestampTypeHandler = new CFBamSaxLoaderTZTimestampType( this );
		}
		return( tZTimestampTypeHandler );
	}
	protected CFBamSaxLoaderTable getTableHandler() {
		if( tableHandler == null ) {
			tableHandler = new CFBamSaxLoaderTable( this );
			tableHandler.addElementHandler( "Relation", getRelationHandler() );
			tableHandler.addElementHandler( "Index", getIndexHandler() );
			tableHandler.addElementHandler( "BlobCol", getBlobColHandler() );
			tableHandler.addElementHandler( "BoolCol", getBoolColHandler() );
			tableHandler.addElementHandler( "DateCol", getDateColHandler() );
			tableHandler.addElementHandler( "DoubleCol", getDoubleColHandler() );
			tableHandler.addElementHandler( "FloatCol", getFloatColHandler() );
			tableHandler.addElementHandler( "Int16Col", getInt16ColHandler() );
			tableHandler.addElementHandler( "Int32Col", getInt32ColHandler() );
			tableHandler.addElementHandler( "Int64Col", getInt64ColHandler() );
			tableHandler.addElementHandler( "NmTokenCol", getNmTokenColHandler() );
			tableHandler.addElementHandler( "NmTokensCol", getNmTokensColHandler() );
			tableHandler.addElementHandler( "NumberCol", getNumberColHandler() );
			tableHandler.addElementHandler( "DbKeyHash128Col", getDbKeyHash128ColHandler() );
			tableHandler.addElementHandler( "DbKeyHash160Col", getDbKeyHash160ColHandler() );
			tableHandler.addElementHandler( "DbKeyHash224Col", getDbKeyHash224ColHandler() );
			tableHandler.addElementHandler( "DbKeyHash256Col", getDbKeyHash256ColHandler() );
			tableHandler.addElementHandler( "DbKeyHash384Col", getDbKeyHash384ColHandler() );
			tableHandler.addElementHandler( "DbKeyHash512Col", getDbKeyHash512ColHandler() );
			tableHandler.addElementHandler( "StringCol", getStringColHandler() );
			tableHandler.addElementHandler( "TZDateCol", getTZDateColHandler() );
			tableHandler.addElementHandler( "TZTimeCol", getTZTimeColHandler() );
			tableHandler.addElementHandler( "TZTimestampCol", getTZTimestampColHandler() );
			tableHandler.addElementHandler( "TextCol", getTextColHandler() );
			tableHandler.addElementHandler( "TimeCol", getTimeColHandler() );
			tableHandler.addElementHandler( "TimestampCol", getTimestampColHandler() );
			tableHandler.addElementHandler( "TokenCol", getTokenColHandler() );
			tableHandler.addElementHandler( "UInt16Col", getUInt16ColHandler() );
			tableHandler.addElementHandler( "UInt32Col", getUInt32ColHandler() );
			tableHandler.addElementHandler( "UInt64Col", getUInt64ColHandler() );
			tableHandler.addElementHandler( "UuidCol", getUuidColHandler() );
			tableHandler.addElementHandler( "Uuid6Col", getUuid6ColHandler() );
			tableHandler.addElementHandler( "TableCol", getTableColHandler() );
			tableHandler.addElementHandler( "Chain", getChainHandler() );
			tableHandler.addElementHandler( "DelTopDep", getDelTopDepHandler() );
			tableHandler.addElementHandler( "ClearTopDep", getClearTopDepHandler() );
			tableHandler.addElementHandler( "ServerMethod", getServerMethodHandler() );
			tableHandler.addElementHandler( "ServerObjFunc", getServerObjFuncHandler() );
			tableHandler.addElementHandler( "ServerProc", getServerProcHandler() );
			tableHandler.addElementHandler( "ServerListFunc", getServerListFuncHandler() );
		}
		return( tableHandler );
	}
	protected CFBamSaxLoaderTableCol getTableColHandler() {
		if( tableColHandler == null ) {
			tableColHandler = new CFBamSaxLoaderTableCol( this );
		}
		return( tableColHandler );
	}
	protected CFBamSaxLoaderTenant getTenantHandler() {
		if( tenantHandler == null ) {
			tenantHandler = new CFBamSaxLoaderTenant( this );
			tenantHandler.addElementHandler( "Tld", getTldHandler() );
		}
		return( tenantHandler );
	}
	protected CFBamSaxLoaderTextCol getTextColHandler() {
		if( textColHandler == null ) {
			textColHandler = new CFBamSaxLoaderTextCol( this );
		}
		return( textColHandler );
	}
	protected CFBamSaxLoaderTextDef getTextDefHandler() {
		if( textDefHandler == null ) {
			textDefHandler = new CFBamSaxLoaderTextDef( this );
		}
		return( textDefHandler );
	}
	protected CFBamSaxLoaderTextType getTextTypeHandler() {
		if( textTypeHandler == null ) {
			textTypeHandler = new CFBamSaxLoaderTextType( this );
		}
		return( textTypeHandler );
	}
	protected CFBamSaxLoaderTimeCol getTimeColHandler() {
		if( timeColHandler == null ) {
			timeColHandler = new CFBamSaxLoaderTimeCol( this );
		}
		return( timeColHandler );
	}
	protected CFBamSaxLoaderTimeDef getTimeDefHandler() {
		if( timeDefHandler == null ) {
			timeDefHandler = new CFBamSaxLoaderTimeDef( this );
		}
		return( timeDefHandler );
	}
	protected CFBamSaxLoaderTimeType getTimeTypeHandler() {
		if( timeTypeHandler == null ) {
			timeTypeHandler = new CFBamSaxLoaderTimeType( this );
		}
		return( timeTypeHandler );
	}
	protected CFBamSaxLoaderTimestampCol getTimestampColHandler() {
		if( timestampColHandler == null ) {
			timestampColHandler = new CFBamSaxLoaderTimestampCol( this );
		}
		return( timestampColHandler );
	}
	protected CFBamSaxLoaderTimestampDef getTimestampDefHandler() {
		if( timestampDefHandler == null ) {
			timestampDefHandler = new CFBamSaxLoaderTimestampDef( this );
		}
		return( timestampDefHandler );
	}
	protected CFBamSaxLoaderTimestampType getTimestampTypeHandler() {
		if( timestampTypeHandler == null ) {
			timestampTypeHandler = new CFBamSaxLoaderTimestampType( this );
		}
		return( timestampTypeHandler );
	}
	protected CFBamSaxLoaderTld getTldHandler() {
		if( tldHandler == null ) {
			tldHandler = new CFBamSaxLoaderTld( this );
			tldHandler.addElementHandler( "TopDomain", getTopDomainHandler() );
		}
		return( tldHandler );
	}
	protected CFBamSaxLoaderTokenCol getTokenColHandler() {
		if( tokenColHandler == null ) {
			tokenColHandler = new CFBamSaxLoaderTokenCol( this );
		}
		return( tokenColHandler );
	}
	protected CFBamSaxLoaderTokenDef getTokenDefHandler() {
		if( tokenDefHandler == null ) {
			tokenDefHandler = new CFBamSaxLoaderTokenDef( this );
		}
		return( tokenDefHandler );
	}
	protected CFBamSaxLoaderTokenType getTokenTypeHandler() {
		if( tokenTypeHandler == null ) {
			tokenTypeHandler = new CFBamSaxLoaderTokenType( this );
		}
		return( tokenTypeHandler );
	}
	protected CFBamSaxLoaderTopDomain getTopDomainHandler() {
		if( topDomainHandler == null ) {
			topDomainHandler = new CFBamSaxLoaderTopDomain( this );
			topDomainHandler.addElementHandler( "TopProject", getTopProjectHandler() );
			topDomainHandler.addElementHandler( "License", getLicenseHandler() );
		}
		return( topDomainHandler );
	}
	protected CFBamSaxLoaderTopProject getTopProjectHandler() {
		if( topProjectHandler == null ) {
			topProjectHandler = new CFBamSaxLoaderTopProject( this );
			topProjectHandler.addElementHandler( "SubProject", getSubProjectHandler() );
		}
		return( topProjectHandler );
	}
	protected CFBamSaxLoaderUInt16Col getUInt16ColHandler() {
		if( uInt16ColHandler == null ) {
			uInt16ColHandler = new CFBamSaxLoaderUInt16Col( this );
		}
		return( uInt16ColHandler );
	}
	protected CFBamSaxLoaderUInt16Def getUInt16DefHandler() {
		if( uInt16DefHandler == null ) {
			uInt16DefHandler = new CFBamSaxLoaderUInt16Def( this );
		}
		return( uInt16DefHandler );
	}
	protected CFBamSaxLoaderUInt16Type getUInt16TypeHandler() {
		if( uInt16TypeHandler == null ) {
			uInt16TypeHandler = new CFBamSaxLoaderUInt16Type( this );
		}
		return( uInt16TypeHandler );
	}
	protected CFBamSaxLoaderUInt32Col getUInt32ColHandler() {
		if( uInt32ColHandler == null ) {
			uInt32ColHandler = new CFBamSaxLoaderUInt32Col( this );
		}
		return( uInt32ColHandler );
	}
	protected CFBamSaxLoaderUInt32Def getUInt32DefHandler() {
		if( uInt32DefHandler == null ) {
			uInt32DefHandler = new CFBamSaxLoaderUInt32Def( this );
		}
		return( uInt32DefHandler );
	}
	protected CFBamSaxLoaderUInt32Type getUInt32TypeHandler() {
		if( uInt32TypeHandler == null ) {
			uInt32TypeHandler = new CFBamSaxLoaderUInt32Type( this );
		}
		return( uInt32TypeHandler );
	}
	protected CFBamSaxLoaderUInt64Col getUInt64ColHandler() {
		if( uInt64ColHandler == null ) {
			uInt64ColHandler = new CFBamSaxLoaderUInt64Col( this );
		}
		return( uInt64ColHandler );
	}
	protected CFBamSaxLoaderUInt64Def getUInt64DefHandler() {
		if( uInt64DefHandler == null ) {
			uInt64DefHandler = new CFBamSaxLoaderUInt64Def( this );
		}
		return( uInt64DefHandler );
	}
	protected CFBamSaxLoaderUInt64Type getUInt64TypeHandler() {
		if( uInt64TypeHandler == null ) {
			uInt64TypeHandler = new CFBamSaxLoaderUInt64Type( this );
		}
		return( uInt64TypeHandler );
	}
	protected CFBamSaxLoaderURLProtocol getURLProtocolHandler() {
		if( uRLProtocolHandler == null ) {
			uRLProtocolHandler = new CFBamSaxLoaderURLProtocol( this );
		}
		return( uRLProtocolHandler );
	}
	protected CFBamSaxLoaderUuid6Col getUuid6ColHandler() {
		if( uuid6ColHandler == null ) {
			uuid6ColHandler = new CFBamSaxLoaderUuid6Col( this );
		}
		return( uuid6ColHandler );
	}
	protected CFBamSaxLoaderUuid6Def getUuid6DefHandler() {
		if( uuid6DefHandler == null ) {
			uuid6DefHandler = new CFBamSaxLoaderUuid6Def( this );
		}
		return( uuid6DefHandler );
	}
	protected CFBamSaxLoaderUuid6Gen getUuid6GenHandler() {
		if( uuid6GenHandler == null ) {
			uuid6GenHandler = new CFBamSaxLoaderUuid6Gen( this );
		}
		return( uuid6GenHandler );
	}
	protected CFBamSaxLoaderUuid6Type getUuid6TypeHandler() {
		if( uuid6TypeHandler == null ) {
			uuid6TypeHandler = new CFBamSaxLoaderUuid6Type( this );
		}
		return( uuid6TypeHandler );
	}
	protected CFBamSaxLoaderUuidCol getUuidColHandler() {
		if( uuidColHandler == null ) {
			uuidColHandler = new CFBamSaxLoaderUuidCol( this );
		}
		return( uuidColHandler );
	}
	protected CFBamSaxLoaderUuidDef getUuidDefHandler() {
		if( uuidDefHandler == null ) {
			uuidDefHandler = new CFBamSaxLoaderUuidDef( this );
		}
		return( uuidDefHandler );
	}
	protected CFBamSaxLoaderUuidGen getUuidGenHandler() {
		if( uuidGenHandler == null ) {
			uuidGenHandler = new CFBamSaxLoaderUuidGen( this );
		}
		return( uuidGenHandler );
	}
	protected CFBamSaxLoaderUuidType getUuidTypeHandler() {
		if( uuidTypeHandler == null ) {
			uuidTypeHandler = new CFBamSaxLoaderUuidType( this );
		}
		return( uuidTypeHandler );
	}
	protected CFBamSaxLoaderValue getValueHandler() {
		if( valueHandler == null ) {
			valueHandler = new CFBamSaxLoaderValue( this );
		}
		return( valueHandler );
	}
	// Root Element Handler Resolver Factory

	protected CFBamSaxRootHandler getSaxRootHandler() {
		if( saxRootHandler == null ) {
			saxRootHandler = new CFBamSaxRootHandler( this );
			saxRootHandler.addElementHandler( "CFBam", getSaxDocHandler() );
			saxRootHandler.addElementHandler( "CFSec", getSaxDocHandler() );
			saxRootHandler.addElementHandler( "CFInt", getSaxDocHandler() );
		}
		return( saxRootHandler );
	}

	// Root Element Handler

	/*
	 *	CFBamSaxRootHandler XML SAX Root Element Handler implementation
	 */
	public class CFBamSaxRootHandler
		extends CFLibXmlCoreElementHandler
	{
		public CFBamSaxRootHandler( CFBamSaxLoader saxLoader ) {
			super( saxLoader );
		}

		public void startElement(
			String		uri,
			String		localName,
			String		qName,
			Attributes	attrs )
		throws SAXException
		{
		}

		public void endElement(
			String		uri,
			String		localName,
			String		qName )
		throws SAXException
		{
		}
	}

	// Document Element Handler Resolver Factory

	protected CFBamSaxDocHandler getSaxDocHandler() {
		if( saxDocHandler == null ) {
			saxDocHandler = new CFBamSaxDocHandler( this );
			saxDocHandler.addElementHandler( "Cluster", getClusterHandler() );
			saxDocHandler.addElementHandler( "ISOCcy", getISOCcyHandler() );
			saxDocHandler.addElementHandler( "ISOCtry", getISOCtryHandler() );
			saxDocHandler.addElementHandler( "ISOLang", getISOLangHandler() );
			saxDocHandler.addElementHandler( "ISOTZone", getISOTZoneHandler() );
			saxDocHandler.addElementHandler( "MimeType", getMimeTypeHandler() );
			saxDocHandler.addElementHandler( "SecClusGrp", getSecClusGrpHandler() );
			saxDocHandler.addElementHandler( "SecClusGrpInc", getSecClusGrpIncHandler() );
			saxDocHandler.addElementHandler( "SecClusGrpMemb", getSecClusGrpMembHandler() );
			saxDocHandler.addElementHandler( "SecSession", getSecSessionHandler() );
			saxDocHandler.addElementHandler( "SecSysGrp", getSecSysGrpHandler() );
			saxDocHandler.addElementHandler( "SecTentGrp", getSecTentGrpHandler() );
			saxDocHandler.addElementHandler( "SecTentGrpInc", getSecTentGrpIncHandler() );
			saxDocHandler.addElementHandler( "SecTentGrpMemb", getSecTentGrpMembHandler() );
			saxDocHandler.addElementHandler( "SecUser", getSecUserHandler() );
			saxDocHandler.addElementHandler( "SecUserPWHistory", getSecUserPWHistoryHandler() );
			saxDocHandler.addElementHandler( "SecUserPassword", getSecUserPasswordHandler() );
			saxDocHandler.addElementHandler( "URLProtocol", getURLProtocolHandler() );
		}
		return( saxDocHandler );
	}

	// Document Element Handler

	/*
	 *	CFBamSaxDocHandler XML SAX Doc Element Handler implementation
	 */
	public class CFBamSaxDocHandler
		extends CFLibXmlCoreElementHandler
	{
		public CFBamSaxDocHandler( CFBamSaxLoader saxLoader ) {
			super( saxLoader );
		}

		public void startElement(
			String		uri,
			String		localName,
			String		qName,
			Attributes	attrs )
		throws SAXException
		{
		}

		public void endElement(
			String		uri,
			String		localName,
			String		qName )
		throws SAXException
		{
		}
	}

	// Loader behaviour configuration accessors

	public LoaderBehaviourEnum getAtomLoaderBehaviour() {
		return( atomLoaderBehaviour );
	}

	public void setAtomLoaderBehaviour( LoaderBehaviourEnum value ) {
		atomLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getBlobColLoaderBehaviour() {
		return( blobColLoaderBehaviour );
	}

	public void setBlobColLoaderBehaviour( LoaderBehaviourEnum value ) {
		blobColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getBlobDefLoaderBehaviour() {
		return( blobDefLoaderBehaviour );
	}

	public void setBlobDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		blobDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getBlobTypeLoaderBehaviour() {
		return( blobTypeLoaderBehaviour );
	}

	public void setBlobTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		blobTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getBoolColLoaderBehaviour() {
		return( boolColLoaderBehaviour );
	}

	public void setBoolColLoaderBehaviour( LoaderBehaviourEnum value ) {
		boolColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getBoolDefLoaderBehaviour() {
		return( boolDefLoaderBehaviour );
	}

	public void setBoolDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		boolDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getBoolTypeLoaderBehaviour() {
		return( boolTypeLoaderBehaviour );
	}

	public void setBoolTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		boolTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getChainLoaderBehaviour() {
		return( chainLoaderBehaviour );
	}

	public void setChainLoaderBehaviour( LoaderBehaviourEnum value ) {
		chainLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getClearDepLoaderBehaviour() {
		return( clearDepLoaderBehaviour );
	}

	public void setClearDepLoaderBehaviour( LoaderBehaviourEnum value ) {
		clearDepLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getClearSubDep1LoaderBehaviour() {
		return( clearSubDep1LoaderBehaviour );
	}

	public void setClearSubDep1LoaderBehaviour( LoaderBehaviourEnum value ) {
		clearSubDep1LoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getClearSubDep2LoaderBehaviour() {
		return( clearSubDep2LoaderBehaviour );
	}

	public void setClearSubDep2LoaderBehaviour( LoaderBehaviourEnum value ) {
		clearSubDep2LoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getClearSubDep3LoaderBehaviour() {
		return( clearSubDep3LoaderBehaviour );
	}

	public void setClearSubDep3LoaderBehaviour( LoaderBehaviourEnum value ) {
		clearSubDep3LoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getClearTopDepLoaderBehaviour() {
		return( clearTopDepLoaderBehaviour );
	}

	public void setClearTopDepLoaderBehaviour( LoaderBehaviourEnum value ) {
		clearTopDepLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getClusterLoaderBehaviour() {
		return( clusterLoaderBehaviour );
	}

	public void setClusterLoaderBehaviour( LoaderBehaviourEnum value ) {
		clusterLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDateColLoaderBehaviour() {
		return( dateColLoaderBehaviour );
	}

	public void setDateColLoaderBehaviour( LoaderBehaviourEnum value ) {
		dateColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDateDefLoaderBehaviour() {
		return( dateDefLoaderBehaviour );
	}

	public void setDateDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		dateDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDateTypeLoaderBehaviour() {
		return( dateTypeLoaderBehaviour );
	}

	public void setDateTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		dateTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash128ColLoaderBehaviour() {
		return( dbKeyHash128ColLoaderBehaviour );
	}

	public void setDbKeyHash128ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash128ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash128DefLoaderBehaviour() {
		return( dbKeyHash128DefLoaderBehaviour );
	}

	public void setDbKeyHash128DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash128DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash128GenLoaderBehaviour() {
		return( dbKeyHash128GenLoaderBehaviour );
	}

	public void setDbKeyHash128GenLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash128GenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash128TypeLoaderBehaviour() {
		return( dbKeyHash128TypeLoaderBehaviour );
	}

	public void setDbKeyHash128TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash128TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash160ColLoaderBehaviour() {
		return( dbKeyHash160ColLoaderBehaviour );
	}

	public void setDbKeyHash160ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash160ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash160DefLoaderBehaviour() {
		return( dbKeyHash160DefLoaderBehaviour );
	}

	public void setDbKeyHash160DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash160DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash160GenLoaderBehaviour() {
		return( dbKeyHash160GenLoaderBehaviour );
	}

	public void setDbKeyHash160GenLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash160GenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash160TypeLoaderBehaviour() {
		return( dbKeyHash160TypeLoaderBehaviour );
	}

	public void setDbKeyHash160TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash160TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash224ColLoaderBehaviour() {
		return( dbKeyHash224ColLoaderBehaviour );
	}

	public void setDbKeyHash224ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash224ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash224DefLoaderBehaviour() {
		return( dbKeyHash224DefLoaderBehaviour );
	}

	public void setDbKeyHash224DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash224DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash224GenLoaderBehaviour() {
		return( dbKeyHash224GenLoaderBehaviour );
	}

	public void setDbKeyHash224GenLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash224GenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash224TypeLoaderBehaviour() {
		return( dbKeyHash224TypeLoaderBehaviour );
	}

	public void setDbKeyHash224TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash224TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash256ColLoaderBehaviour() {
		return( dbKeyHash256ColLoaderBehaviour );
	}

	public void setDbKeyHash256ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash256ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash256DefLoaderBehaviour() {
		return( dbKeyHash256DefLoaderBehaviour );
	}

	public void setDbKeyHash256DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash256DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash256GenLoaderBehaviour() {
		return( dbKeyHash256GenLoaderBehaviour );
	}

	public void setDbKeyHash256GenLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash256GenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash256TypeLoaderBehaviour() {
		return( dbKeyHash256TypeLoaderBehaviour );
	}

	public void setDbKeyHash256TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash256TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash384ColLoaderBehaviour() {
		return( dbKeyHash384ColLoaderBehaviour );
	}

	public void setDbKeyHash384ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash384ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash384DefLoaderBehaviour() {
		return( dbKeyHash384DefLoaderBehaviour );
	}

	public void setDbKeyHash384DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash384DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash384GenLoaderBehaviour() {
		return( dbKeyHash384GenLoaderBehaviour );
	}

	public void setDbKeyHash384GenLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash384GenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash384TypeLoaderBehaviour() {
		return( dbKeyHash384TypeLoaderBehaviour );
	}

	public void setDbKeyHash384TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash384TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash512ColLoaderBehaviour() {
		return( dbKeyHash512ColLoaderBehaviour );
	}

	public void setDbKeyHash512ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash512ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash512DefLoaderBehaviour() {
		return( dbKeyHash512DefLoaderBehaviour );
	}

	public void setDbKeyHash512DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash512DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash512GenLoaderBehaviour() {
		return( dbKeyHash512GenLoaderBehaviour );
	}

	public void setDbKeyHash512GenLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash512GenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDbKeyHash512TypeLoaderBehaviour() {
		return( dbKeyHash512TypeLoaderBehaviour );
	}

	public void setDbKeyHash512TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		dbKeyHash512TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDelDepLoaderBehaviour() {
		return( delDepLoaderBehaviour );
	}

	public void setDelDepLoaderBehaviour( LoaderBehaviourEnum value ) {
		delDepLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDelSubDep1LoaderBehaviour() {
		return( delSubDep1LoaderBehaviour );
	}

	public void setDelSubDep1LoaderBehaviour( LoaderBehaviourEnum value ) {
		delSubDep1LoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDelSubDep2LoaderBehaviour() {
		return( delSubDep2LoaderBehaviour );
	}

	public void setDelSubDep2LoaderBehaviour( LoaderBehaviourEnum value ) {
		delSubDep2LoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDelSubDep3LoaderBehaviour() {
		return( delSubDep3LoaderBehaviour );
	}

	public void setDelSubDep3LoaderBehaviour( LoaderBehaviourEnum value ) {
		delSubDep3LoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDelTopDepLoaderBehaviour() {
		return( delTopDepLoaderBehaviour );
	}

	public void setDelTopDepLoaderBehaviour( LoaderBehaviourEnum value ) {
		delTopDepLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDoubleColLoaderBehaviour() {
		return( doubleColLoaderBehaviour );
	}

	public void setDoubleColLoaderBehaviour( LoaderBehaviourEnum value ) {
		doubleColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDoubleDefLoaderBehaviour() {
		return( doubleDefLoaderBehaviour );
	}

	public void setDoubleDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		doubleDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getDoubleTypeLoaderBehaviour() {
		return( doubleTypeLoaderBehaviour );
	}

	public void setDoubleTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		doubleTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getEnumDefLoaderBehaviour() {
		return( enumDefLoaderBehaviour );
	}

	public void setEnumDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		enumDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getEnumTagLoaderBehaviour() {
		return( enumTagLoaderBehaviour );
	}

	public void setEnumTagLoaderBehaviour( LoaderBehaviourEnum value ) {
		enumTagLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getEnumTypeLoaderBehaviour() {
		return( enumTypeLoaderBehaviour );
	}

	public void setEnumTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		enumTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getFloatColLoaderBehaviour() {
		return( floatColLoaderBehaviour );
	}

	public void setFloatColLoaderBehaviour( LoaderBehaviourEnum value ) {
		floatColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getFloatDefLoaderBehaviour() {
		return( floatDefLoaderBehaviour );
	}

	public void setFloatDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		floatDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getFloatTypeLoaderBehaviour() {
		return( floatTypeLoaderBehaviour );
	}

	public void setFloatTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		floatTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOCcyLoaderBehaviour() {
		return( iSOCcyLoaderBehaviour );
	}

	public void setISOCcyLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOCcyLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOCtryLoaderBehaviour() {
		return( iSOCtryLoaderBehaviour );
	}

	public void setISOCtryLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOCtryLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOCtryCcyLoaderBehaviour() {
		return( iSOCtryCcyLoaderBehaviour );
	}

	public void setISOCtryCcyLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOCtryCcyLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOCtryLangLoaderBehaviour() {
		return( iSOCtryLangLoaderBehaviour );
	}

	public void setISOCtryLangLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOCtryLangLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOLangLoaderBehaviour() {
		return( iSOLangLoaderBehaviour );
	}

	public void setISOLangLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOLangLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOTZoneLoaderBehaviour() {
		return( iSOTZoneLoaderBehaviour );
	}

	public void setISOTZoneLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOTZoneLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getId16GenLoaderBehaviour() {
		return( id16GenLoaderBehaviour );
	}

	public void setId16GenLoaderBehaviour( LoaderBehaviourEnum value ) {
		id16GenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getId32GenLoaderBehaviour() {
		return( id32GenLoaderBehaviour );
	}

	public void setId32GenLoaderBehaviour( LoaderBehaviourEnum value ) {
		id32GenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getId64GenLoaderBehaviour() {
		return( id64GenLoaderBehaviour );
	}

	public void setId64GenLoaderBehaviour( LoaderBehaviourEnum value ) {
		id64GenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getIndexLoaderBehaviour() {
		return( indexLoaderBehaviour );
	}

	public void setIndexLoaderBehaviour( LoaderBehaviourEnum value ) {
		indexLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getIndexColLoaderBehaviour() {
		return( indexColLoaderBehaviour );
	}

	public void setIndexColLoaderBehaviour( LoaderBehaviourEnum value ) {
		indexColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getInt16ColLoaderBehaviour() {
		return( int16ColLoaderBehaviour );
	}

	public void setInt16ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		int16ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getInt16DefLoaderBehaviour() {
		return( int16DefLoaderBehaviour );
	}

	public void setInt16DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		int16DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getInt16TypeLoaderBehaviour() {
		return( int16TypeLoaderBehaviour );
	}

	public void setInt16TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		int16TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getInt32ColLoaderBehaviour() {
		return( int32ColLoaderBehaviour );
	}

	public void setInt32ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		int32ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getInt32DefLoaderBehaviour() {
		return( int32DefLoaderBehaviour );
	}

	public void setInt32DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		int32DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getInt32TypeLoaderBehaviour() {
		return( int32TypeLoaderBehaviour );
	}

	public void setInt32TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		int32TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getInt64ColLoaderBehaviour() {
		return( int64ColLoaderBehaviour );
	}

	public void setInt64ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		int64ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getInt64DefLoaderBehaviour() {
		return( int64DefLoaderBehaviour );
	}

	public void setInt64DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		int64DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getInt64TypeLoaderBehaviour() {
		return( int64TypeLoaderBehaviour );
	}

	public void setInt64TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		int64TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getLicenseLoaderBehaviour() {
		return( licenseLoaderBehaviour );
	}

	public void setLicenseLoaderBehaviour( LoaderBehaviourEnum value ) {
		licenseLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getMajorVersionLoaderBehaviour() {
		return( majorVersionLoaderBehaviour );
	}

	public void setMajorVersionLoaderBehaviour( LoaderBehaviourEnum value ) {
		majorVersionLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getMimeTypeLoaderBehaviour() {
		return( mimeTypeLoaderBehaviour );
	}

	public void setMimeTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		mimeTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getMinorVersionLoaderBehaviour() {
		return( minorVersionLoaderBehaviour );
	}

	public void setMinorVersionLoaderBehaviour( LoaderBehaviourEnum value ) {
		minorVersionLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getNmTokenColLoaderBehaviour() {
		return( nmTokenColLoaderBehaviour );
	}

	public void setNmTokenColLoaderBehaviour( LoaderBehaviourEnum value ) {
		nmTokenColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getNmTokenDefLoaderBehaviour() {
		return( nmTokenDefLoaderBehaviour );
	}

	public void setNmTokenDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		nmTokenDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getNmTokenTypeLoaderBehaviour() {
		return( nmTokenTypeLoaderBehaviour );
	}

	public void setNmTokenTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		nmTokenTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getNmTokensColLoaderBehaviour() {
		return( nmTokensColLoaderBehaviour );
	}

	public void setNmTokensColLoaderBehaviour( LoaderBehaviourEnum value ) {
		nmTokensColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getNmTokensDefLoaderBehaviour() {
		return( nmTokensDefLoaderBehaviour );
	}

	public void setNmTokensDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		nmTokensDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getNmTokensTypeLoaderBehaviour() {
		return( nmTokensTypeLoaderBehaviour );
	}

	public void setNmTokensTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		nmTokensTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getNumberColLoaderBehaviour() {
		return( numberColLoaderBehaviour );
	}

	public void setNumberColLoaderBehaviour( LoaderBehaviourEnum value ) {
		numberColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getNumberDefLoaderBehaviour() {
		return( numberDefLoaderBehaviour );
	}

	public void setNumberDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		numberDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getNumberTypeLoaderBehaviour() {
		return( numberTypeLoaderBehaviour );
	}

	public void setNumberTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		numberTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getParamLoaderBehaviour() {
		return( paramLoaderBehaviour );
	}

	public void setParamLoaderBehaviour( LoaderBehaviourEnum value ) {
		paramLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getPopDepLoaderBehaviour() {
		return( popDepLoaderBehaviour );
	}

	public void setPopDepLoaderBehaviour( LoaderBehaviourEnum value ) {
		popDepLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getPopSubDep1LoaderBehaviour() {
		return( popSubDep1LoaderBehaviour );
	}

	public void setPopSubDep1LoaderBehaviour( LoaderBehaviourEnum value ) {
		popSubDep1LoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getPopSubDep2LoaderBehaviour() {
		return( popSubDep2LoaderBehaviour );
	}

	public void setPopSubDep2LoaderBehaviour( LoaderBehaviourEnum value ) {
		popSubDep2LoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getPopSubDep3LoaderBehaviour() {
		return( popSubDep3LoaderBehaviour );
	}

	public void setPopSubDep3LoaderBehaviour( LoaderBehaviourEnum value ) {
		popSubDep3LoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getPopTopDepLoaderBehaviour() {
		return( popTopDepLoaderBehaviour );
	}

	public void setPopTopDepLoaderBehaviour( LoaderBehaviourEnum value ) {
		popTopDepLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getRelationLoaderBehaviour() {
		return( relationLoaderBehaviour );
	}

	public void setRelationLoaderBehaviour( LoaderBehaviourEnum value ) {
		relationLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getRelationColLoaderBehaviour() {
		return( relationColLoaderBehaviour );
	}

	public void setRelationColLoaderBehaviour( LoaderBehaviourEnum value ) {
		relationColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSchemaDefLoaderBehaviour() {
		return( schemaDefLoaderBehaviour );
	}

	public void setSchemaDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		schemaDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSchemaRefLoaderBehaviour() {
		return( schemaRefLoaderBehaviour );
	}

	public void setSchemaRefLoaderBehaviour( LoaderBehaviourEnum value ) {
		schemaRefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getScopeLoaderBehaviour() {
		return( scopeLoaderBehaviour );
	}

	public void setScopeLoaderBehaviour( LoaderBehaviourEnum value ) {
		scopeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecClusGrpLoaderBehaviour() {
		return( secClusGrpLoaderBehaviour );
	}

	public void setSecClusGrpLoaderBehaviour( LoaderBehaviourEnum value ) {
		secClusGrpLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecClusGrpIncLoaderBehaviour() {
		return( secClusGrpIncLoaderBehaviour );
	}

	public void setSecClusGrpIncLoaderBehaviour( LoaderBehaviourEnum value ) {
		secClusGrpIncLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecClusGrpMembLoaderBehaviour() {
		return( secClusGrpMembLoaderBehaviour );
	}

	public void setSecClusGrpMembLoaderBehaviour( LoaderBehaviourEnum value ) {
		secClusGrpMembLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecSessionLoaderBehaviour() {
		return( secSessionLoaderBehaviour );
	}

	public void setSecSessionLoaderBehaviour( LoaderBehaviourEnum value ) {
		secSessionLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecSysGrpLoaderBehaviour() {
		return( secSysGrpLoaderBehaviour );
	}

	public void setSecSysGrpLoaderBehaviour( LoaderBehaviourEnum value ) {
		secSysGrpLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecSysGrpIncLoaderBehaviour() {
		return( secSysGrpIncLoaderBehaviour );
	}

	public void setSecSysGrpIncLoaderBehaviour( LoaderBehaviourEnum value ) {
		secSysGrpIncLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecSysGrpMembLoaderBehaviour() {
		return( secSysGrpMembLoaderBehaviour );
	}

	public void setSecSysGrpMembLoaderBehaviour( LoaderBehaviourEnum value ) {
		secSysGrpMembLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecTentGrpLoaderBehaviour() {
		return( secTentGrpLoaderBehaviour );
	}

	public void setSecTentGrpLoaderBehaviour( LoaderBehaviourEnum value ) {
		secTentGrpLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecTentGrpIncLoaderBehaviour() {
		return( secTentGrpIncLoaderBehaviour );
	}

	public void setSecTentGrpIncLoaderBehaviour( LoaderBehaviourEnum value ) {
		secTentGrpIncLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecTentGrpMembLoaderBehaviour() {
		return( secTentGrpMembLoaderBehaviour );
	}

	public void setSecTentGrpMembLoaderBehaviour( LoaderBehaviourEnum value ) {
		secTentGrpMembLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecUserLoaderBehaviour() {
		return( secUserLoaderBehaviour );
	}

	public void setSecUserLoaderBehaviour( LoaderBehaviourEnum value ) {
		secUserLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecUserPWHistoryLoaderBehaviour() {
		return( secUserPWHistoryLoaderBehaviour );
	}

	public void setSecUserPWHistoryLoaderBehaviour( LoaderBehaviourEnum value ) {
		secUserPWHistoryLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecUserPasswordLoaderBehaviour() {
		return( secUserPasswordLoaderBehaviour );
	}

	public void setSecUserPasswordLoaderBehaviour( LoaderBehaviourEnum value ) {
		secUserPasswordLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getServerListFuncLoaderBehaviour() {
		return( serverListFuncLoaderBehaviour );
	}

	public void setServerListFuncLoaderBehaviour( LoaderBehaviourEnum value ) {
		serverListFuncLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getServerMethodLoaderBehaviour() {
		return( serverMethodLoaderBehaviour );
	}

	public void setServerMethodLoaderBehaviour( LoaderBehaviourEnum value ) {
		serverMethodLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getServerObjFuncLoaderBehaviour() {
		return( serverObjFuncLoaderBehaviour );
	}

	public void setServerObjFuncLoaderBehaviour( LoaderBehaviourEnum value ) {
		serverObjFuncLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getServerProcLoaderBehaviour() {
		return( serverProcLoaderBehaviour );
	}

	public void setServerProcLoaderBehaviour( LoaderBehaviourEnum value ) {
		serverProcLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getStringColLoaderBehaviour() {
		return( stringColLoaderBehaviour );
	}

	public void setStringColLoaderBehaviour( LoaderBehaviourEnum value ) {
		stringColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getStringDefLoaderBehaviour() {
		return( stringDefLoaderBehaviour );
	}

	public void setStringDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		stringDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getStringTypeLoaderBehaviour() {
		return( stringTypeLoaderBehaviour );
	}

	public void setStringTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		stringTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSubProjectLoaderBehaviour() {
		return( subProjectLoaderBehaviour );
	}

	public void setSubProjectLoaderBehaviour( LoaderBehaviourEnum value ) {
		subProjectLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSysClusterLoaderBehaviour() {
		return( sysClusterLoaderBehaviour );
	}

	public void setSysClusterLoaderBehaviour( LoaderBehaviourEnum value ) {
		sysClusterLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTZDateColLoaderBehaviour() {
		return( tZDateColLoaderBehaviour );
	}

	public void setTZDateColLoaderBehaviour( LoaderBehaviourEnum value ) {
		tZDateColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTZDateDefLoaderBehaviour() {
		return( tZDateDefLoaderBehaviour );
	}

	public void setTZDateDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		tZDateDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTZDateTypeLoaderBehaviour() {
		return( tZDateTypeLoaderBehaviour );
	}

	public void setTZDateTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		tZDateTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTZTimeColLoaderBehaviour() {
		return( tZTimeColLoaderBehaviour );
	}

	public void setTZTimeColLoaderBehaviour( LoaderBehaviourEnum value ) {
		tZTimeColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTZTimeDefLoaderBehaviour() {
		return( tZTimeDefLoaderBehaviour );
	}

	public void setTZTimeDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		tZTimeDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTZTimeTypeLoaderBehaviour() {
		return( tZTimeTypeLoaderBehaviour );
	}

	public void setTZTimeTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		tZTimeTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTZTimestampColLoaderBehaviour() {
		return( tZTimestampColLoaderBehaviour );
	}

	public void setTZTimestampColLoaderBehaviour( LoaderBehaviourEnum value ) {
		tZTimestampColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTZTimestampDefLoaderBehaviour() {
		return( tZTimestampDefLoaderBehaviour );
	}

	public void setTZTimestampDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		tZTimestampDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTZTimestampTypeLoaderBehaviour() {
		return( tZTimestampTypeLoaderBehaviour );
	}

	public void setTZTimestampTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		tZTimestampTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTableLoaderBehaviour() {
		return( tableLoaderBehaviour );
	}

	public void setTableLoaderBehaviour( LoaderBehaviourEnum value ) {
		tableLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTableColLoaderBehaviour() {
		return( tableColLoaderBehaviour );
	}

	public void setTableColLoaderBehaviour( LoaderBehaviourEnum value ) {
		tableColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTenantLoaderBehaviour() {
		return( tenantLoaderBehaviour );
	}

	public void setTenantLoaderBehaviour( LoaderBehaviourEnum value ) {
		tenantLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTextColLoaderBehaviour() {
		return( textColLoaderBehaviour );
	}

	public void setTextColLoaderBehaviour( LoaderBehaviourEnum value ) {
		textColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTextDefLoaderBehaviour() {
		return( textDefLoaderBehaviour );
	}

	public void setTextDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		textDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTextTypeLoaderBehaviour() {
		return( textTypeLoaderBehaviour );
	}

	public void setTextTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		textTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTimeColLoaderBehaviour() {
		return( timeColLoaderBehaviour );
	}

	public void setTimeColLoaderBehaviour( LoaderBehaviourEnum value ) {
		timeColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTimeDefLoaderBehaviour() {
		return( timeDefLoaderBehaviour );
	}

	public void setTimeDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		timeDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTimeTypeLoaderBehaviour() {
		return( timeTypeLoaderBehaviour );
	}

	public void setTimeTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		timeTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTimestampColLoaderBehaviour() {
		return( timestampColLoaderBehaviour );
	}

	public void setTimestampColLoaderBehaviour( LoaderBehaviourEnum value ) {
		timestampColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTimestampDefLoaderBehaviour() {
		return( timestampDefLoaderBehaviour );
	}

	public void setTimestampDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		timestampDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTimestampTypeLoaderBehaviour() {
		return( timestampTypeLoaderBehaviour );
	}

	public void setTimestampTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		timestampTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTldLoaderBehaviour() {
		return( tldLoaderBehaviour );
	}

	public void setTldLoaderBehaviour( LoaderBehaviourEnum value ) {
		tldLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTokenColLoaderBehaviour() {
		return( tokenColLoaderBehaviour );
	}

	public void setTokenColLoaderBehaviour( LoaderBehaviourEnum value ) {
		tokenColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTokenDefLoaderBehaviour() {
		return( tokenDefLoaderBehaviour );
	}

	public void setTokenDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		tokenDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTokenTypeLoaderBehaviour() {
		return( tokenTypeLoaderBehaviour );
	}

	public void setTokenTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		tokenTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTopDomainLoaderBehaviour() {
		return( topDomainLoaderBehaviour );
	}

	public void setTopDomainLoaderBehaviour( LoaderBehaviourEnum value ) {
		topDomainLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTopProjectLoaderBehaviour() {
		return( topProjectLoaderBehaviour );
	}

	public void setTopProjectLoaderBehaviour( LoaderBehaviourEnum value ) {
		topProjectLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUInt16ColLoaderBehaviour() {
		return( uInt16ColLoaderBehaviour );
	}

	public void setUInt16ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		uInt16ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUInt16DefLoaderBehaviour() {
		return( uInt16DefLoaderBehaviour );
	}

	public void setUInt16DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		uInt16DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUInt16TypeLoaderBehaviour() {
		return( uInt16TypeLoaderBehaviour );
	}

	public void setUInt16TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		uInt16TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUInt32ColLoaderBehaviour() {
		return( uInt32ColLoaderBehaviour );
	}

	public void setUInt32ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		uInt32ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUInt32DefLoaderBehaviour() {
		return( uInt32DefLoaderBehaviour );
	}

	public void setUInt32DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		uInt32DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUInt32TypeLoaderBehaviour() {
		return( uInt32TypeLoaderBehaviour );
	}

	public void setUInt32TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		uInt32TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUInt64ColLoaderBehaviour() {
		return( uInt64ColLoaderBehaviour );
	}

	public void setUInt64ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		uInt64ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUInt64DefLoaderBehaviour() {
		return( uInt64DefLoaderBehaviour );
	}

	public void setUInt64DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		uInt64DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUInt64TypeLoaderBehaviour() {
		return( uInt64TypeLoaderBehaviour );
	}

	public void setUInt64TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		uInt64TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getURLProtocolLoaderBehaviour() {
		return( uRLProtocolLoaderBehaviour );
	}

	public void setURLProtocolLoaderBehaviour( LoaderBehaviourEnum value ) {
		uRLProtocolLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUuid6ColLoaderBehaviour() {
		return( uuid6ColLoaderBehaviour );
	}

	public void setUuid6ColLoaderBehaviour( LoaderBehaviourEnum value ) {
		uuid6ColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUuid6DefLoaderBehaviour() {
		return( uuid6DefLoaderBehaviour );
	}

	public void setUuid6DefLoaderBehaviour( LoaderBehaviourEnum value ) {
		uuid6DefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUuid6GenLoaderBehaviour() {
		return( uuid6GenLoaderBehaviour );
	}

	public void setUuid6GenLoaderBehaviour( LoaderBehaviourEnum value ) {
		uuid6GenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUuid6TypeLoaderBehaviour() {
		return( uuid6TypeLoaderBehaviour );
	}

	public void setUuid6TypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		uuid6TypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUuidColLoaderBehaviour() {
		return( uuidColLoaderBehaviour );
	}

	public void setUuidColLoaderBehaviour( LoaderBehaviourEnum value ) {
		uuidColLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUuidDefLoaderBehaviour() {
		return( uuidDefLoaderBehaviour );
	}

	public void setUuidDefLoaderBehaviour( LoaderBehaviourEnum value ) {
		uuidDefLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUuidGenLoaderBehaviour() {
		return( uuidGenLoaderBehaviour );
	}

	public void setUuidGenLoaderBehaviour( LoaderBehaviourEnum value ) {
		uuidGenLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getUuidTypeLoaderBehaviour() {
		return( uuidTypeLoaderBehaviour );
	}

	public void setUuidTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		uuidTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getValueLoaderBehaviour() {
		return( valueLoaderBehaviour );
	}

	public void setValueLoaderBehaviour( LoaderBehaviourEnum value ) {
		valueLoaderBehaviour = value;
	}

	// Parse a file

	public void parseFile( String url ) {
		parse( url );
	}
}
