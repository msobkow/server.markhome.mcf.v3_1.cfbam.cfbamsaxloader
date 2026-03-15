
// Description: Java 25 XML SAX Element Handler for ServerListFunc

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

import java.math.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.xml.sax.*;
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

/*
 *	CFBamSaxLoaderServerListFuncParse XML SAX Element Handler implementation
 *	for ServerListFunc.
 */
public class CFBamSaxLoaderServerListFunc
	extends CFLibXmlCoreElementHandler
{
	public CFBamSaxLoaderServerListFunc( CFBamSaxLoader saxLoader ) {
		super( saxLoader );
	}

	public void startElement(
		String		uri,
		String		localName,
		String		qName,
		Attributes	attrs )
	throws SAXException
	{
		final String S_ProcName = "startElement";
		ICFBamServerListFuncObj origBuff = null;
		ICFBamServerListFuncEditObj editBuff = null;
		// Common XML Attributes
		String attrId = null;
		// Scope Attributes
		// Scope References
		ICFBamTenantObj refTenant = null;
		// ServerMethod Attributes
		String attrName = null;
		String attrShortName = null;
		String attrLabel = null;
		String attrShortDescription = null;
		String attrDescription = null;
		String attrSuffix = null;
		String attrIsInstanceMethod = null;
		String attrIsServerOnly = null;
		String attrJMethodBody = null;
		String attrCppMethodBody = null;
		String attrCsMethodBody = null;
		String attrDefSchema = null;
		// ServerMethod References
		ICFBamSchemaDefObj refDefSchema = null;
		ICFBamTableObj refForTable = null;
		// ServerListFunc Attributes
		String attrRetTable = null;
		// ServerListFunc References
		ICFBamTableObj refRetTable = null;
		// Attribute Extraction
		String attrLocalName;
		int numAttrs;
		int idxAttr;
		final String S_LocalName = "LocalName";
		try {
			assert qName.equals( "ServerListFunc" );

			CFBamSaxLoader saxLoader = (CFBamSaxLoader)getParser();
			if( saxLoader == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"getParser()" );
			}

			ICFBamSchemaObj schemaObj = saxLoader.getSchemaObj();
			if( schemaObj == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"getParser().getSchemaObj()" );
			}

			// Instantiate an edit buffer for the parsed information
			origBuff = (ICFBamServerListFuncObj)schemaObj.getServerListFuncTableObj().newInstance();
			editBuff = (ICFBamServerListFuncEditObj)origBuff.beginEdit();

			// Extract Attributes
			numAttrs = attrs.getLength();
			for( idxAttr = 0; idxAttr < numAttrs; idxAttr++ ) {
				attrLocalName = attrs.getLocalName( idxAttr );
				if( attrLocalName.equals( "Id" ) ) {
					if( attrId != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrId = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Name" ) ) {
					if( attrName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ShortName" ) ) {
					if( attrShortName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrShortName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Label" ) ) {
					if( attrLabel != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrLabel = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ShortDescription" ) ) {
					if( attrShortDescription != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrShortDescription = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Description" ) ) {
					if( attrDescription != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrDescription = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Suffix" ) ) {
					if( attrSuffix != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrSuffix = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "IsInstanceMethod" ) ) {
					if( attrIsInstanceMethod != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrIsInstanceMethod = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "IsServerOnly" ) ) {
					if( attrIsServerOnly != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrIsServerOnly = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "JMethodBody" ) ) {
					if( attrJMethodBody != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrJMethodBody = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "CppMethodBody" ) ) {
					if( attrCppMethodBody != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrCppMethodBody = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "CsMethodBody" ) ) {
					if( attrCsMethodBody != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrCsMethodBody = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "DefSchema" ) ) {
					if( attrDefSchema != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrDefSchema = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "RetTable" ) ) {
					if( attrRetTable != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrRetTable = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "schemaLocation" ) ) {
					// ignored
				}
				else {
					throw new CFLibUnrecognizedAttributeException( getClass(),
						S_ProcName,
						getParser().getLocationInfo(),
						attrLocalName );
				}
			}

			// Ensure that required attributes have values
			if( attrName == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"Name" );
			}
			if( ( attrIsInstanceMethod == null ) || ( attrIsInstanceMethod.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"IsInstanceMethod" );
			}
			if( ( attrIsServerOnly == null ) || ( attrIsServerOnly.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"IsServerOnly" );
			}
			if( attrJMethodBody == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"JMethodBody" );
			}
			if( attrCppMethodBody == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"CppMethodBody" );
			}
			if( attrCsMethodBody == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"CsMethodBody" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "Name", attrName );
			curContext.putNamedValue( "ShortName", attrShortName );
			curContext.putNamedValue( "Label", attrLabel );
			curContext.putNamedValue( "ShortDescription", attrShortDescription );
			curContext.putNamedValue( "Description", attrDescription );
			curContext.putNamedValue( "Suffix", attrSuffix );
			curContext.putNamedValue( "IsInstanceMethod", attrIsInstanceMethod );
			curContext.putNamedValue( "IsServerOnly", attrIsServerOnly );
			curContext.putNamedValue( "JMethodBody", attrJMethodBody );
			curContext.putNamedValue( "CppMethodBody", attrCppMethodBody );
			curContext.putNamedValue( "CsMethodBody", attrCsMethodBody );
			curContext.putNamedValue( "DefSchema", attrDefSchema );
			curContext.putNamedValue( "RetTable", attrRetTable );

			// Convert string attributes to native Java types
			// and apply the converted attributes to the editBuff.

			Integer natId;
			if( ( attrId != null ) && ( attrId.length() > 0 ) ) {
				natId = Integer.valueOf( Integer.parseInt( attrId ) );
			}
			else {
				natId = null;
			}
			String natName = attrName;
			editBuff.setRequiredName( natName );

			String natShortName = attrShortName;
			editBuff.setOptionalShortName( natShortName );

			String natLabel = attrLabel;
			editBuff.setOptionalLabel( natLabel );

			String natShortDescription = attrShortDescription;
			editBuff.setOptionalShortDescription( natShortDescription );

			String natDescription = attrDescription;
			editBuff.setOptionalDescription( natDescription );

			String natSuffix = attrSuffix;
			editBuff.setOptionalSuffix( natSuffix );

			boolean natIsInstanceMethod;
			if( attrIsInstanceMethod.equals( "true" ) || attrIsInstanceMethod.equals( "yes" ) || attrIsInstanceMethod.equals( "1" ) ) {
				natIsInstanceMethod = true;
			}
			else if( attrIsInstanceMethod.equals( "false" ) || attrIsInstanceMethod.equals( "no" ) || attrIsInstanceMethod.equals( "0" ) ) {
				natIsInstanceMethod = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "IsInstanceMethod", attrIsInstanceMethod),
					String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "IsInstanceMethod", attrIsInstanceMethod));
			}
			editBuff.setRequiredIsInstanceMethod( natIsInstanceMethod );

			boolean natIsServerOnly;
			if( attrIsServerOnly.equals( "true" ) || attrIsServerOnly.equals( "yes" ) || attrIsServerOnly.equals( "1" ) ) {
				natIsServerOnly = true;
			}
			else if( attrIsServerOnly.equals( "false" ) || attrIsServerOnly.equals( "no" ) || attrIsServerOnly.equals( "0" ) ) {
				natIsServerOnly = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "IsServerOnly", attrIsServerOnly),
					String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "IsServerOnly", attrIsServerOnly));
			}
			editBuff.setRequiredIsServerOnly( natIsServerOnly );

			String natJMethodBody = attrJMethodBody;
			editBuff.setRequiredJMethodBody( natJMethodBody );

			String natCppMethodBody = attrCppMethodBody;
			editBuff.setRequiredCppMethodBody( natCppMethodBody );

			String natCsMethodBody = attrCsMethodBody;
			editBuff.setRequiredCsMethodBody( natCsMethodBody );

			// Get the scope/container object

			CFLibXmlCoreContext parentContext = curContext.getPrevContext();
			Object scopeObj;
			if( parentContext != null ) {
				scopeObj = parentContext.getNamedValue( "Object" );
			}
			else {
				scopeObj = null;
			}

			// Resolve and apply required Container reference

			if( scopeObj == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"scopeObj" );
			}
			else if( scopeObj instanceof ICFBamTableObj ) {
				refForTable = (ICFBamTableObj) scopeObj;
				editBuff.setRequiredContainerForTable( refForTable );
				refTenant = (ICFBamTenantObj)editBuff.getRequiredOwnerTenant();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFBamTableObj" );
			}

			// Resolve and apply Owner reference

			if( refTenant == null ) {
				if( scopeObj instanceof ICFBamTenantObj ) {
					refTenant = (ICFBamTenantObj) scopeObj;
					editBuff.setRequiredOwnerTenant( refTenant );
				}
				else {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Owner<Tenant>" );
				}
			}

			// Lookup refDefSchema by qualified name
			if( ( attrDefSchema != null ) && ( attrDefSchema.length() > 0 ) ) {
				refDefSchema = (ICFBamSchemaDefObj)(editBuff.getNamedObject( schemaObj.getSchemaDefTableObj().getObjQualifyingClass(),
					attrDefSchema ) );
				if( refDefSchema == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve DefSchema reference qualified name \"" + attrDefSchema + "\" to table SchemaDef" );
				}
			}
			else {
				refDefSchema = null;
			}
			editBuff.setOptionalLookupDefSchema( refDefSchema );

			// Lookup refRetTable by qualified name
			if( ( attrRetTable != null ) && ( attrRetTable.length() > 0 ) ) {
				refRetTable = (ICFBamTableObj)(editBuff.getNamedObject( schemaObj.getTableTableObj().getObjQualifyingClass(),
					attrRetTable ) );
				if( refRetTable == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve RetTable reference qualified name \"" + attrRetTable + "\" to table Table" );
				}
			}
			else {
				refRetTable = null;
			}
			editBuff.setOptionalLookupRetTable( refRetTable );

			CFBamSaxLoader.LoaderBehaviourEnum loaderBehaviour = saxLoader.getServerListFuncLoaderBehaviour();
			ICFBamServerListFuncEditObj editServerListFunc = null;
			ICFBamServerListFuncObj origServerListFunc = (ICFBamServerListFuncObj)schemaObj.getServerListFuncTableObj().readServerListFuncByUNameIdx( refForTable.getRequiredId(),
			editBuff.getRequiredName() );
			if( origServerListFunc == null ) {
				editServerListFunc = editBuff;
			}
			else {
				switch( loaderBehaviour ) {
					case Insert:
						break;
					case Update:
						editServerListFunc = (ICFBamServerListFuncEditObj)origServerListFunc.beginEdit();
						editServerListFunc.setRequiredName( editBuff.getRequiredName() );
						editServerListFunc.setOptionalShortName( editBuff.getOptionalShortName() );
						editServerListFunc.setOptionalLabel( editBuff.getOptionalLabel() );
						editServerListFunc.setOptionalShortDescription( editBuff.getOptionalShortDescription() );
						editServerListFunc.setOptionalDescription( editBuff.getOptionalDescription() );
						editServerListFunc.setOptionalSuffix( editBuff.getOptionalSuffix() );
						editServerListFunc.setRequiredIsInstanceMethod( editBuff.getRequiredIsInstanceMethod() );
						editServerListFunc.setRequiredIsServerOnly( editBuff.getRequiredIsServerOnly() );
						editServerListFunc.setRequiredJMethodBody( editBuff.getRequiredJMethodBody() );
						editServerListFunc.setRequiredCppMethodBody( editBuff.getRequiredCppMethodBody() );
						editServerListFunc.setRequiredCsMethodBody( editBuff.getRequiredCsMethodBody() );
						editServerListFunc.setOptionalLookupDefSchema( editBuff.getOptionalLookupDefSchema() );
						editServerListFunc.setOptionalLookupRetTable( editBuff.getOptionalLookupRetTable() );
						break;
					case Replace:
						editServerListFunc = (ICFBamServerListFuncEditObj)origServerListFunc.beginEdit();
						editServerListFunc.deleteInstance();
						editServerListFunc = null;
						origServerListFunc = null;
						editServerListFunc = editBuff;
						break;
				}
			}

			if( editServerListFunc != null ) {
				if( origServerListFunc != null ) {
					editServerListFunc.update();
				}
				else {
					origServerListFunc = (ICFBamServerListFuncObj)editServerListFunc.create();
				}
				editServerListFunc = null;
			}

			curContext.putNamedValue( "Object", origServerListFunc );
		}
		catch( RuntimeException e ) {
			throw new SAXException( "Near " + getParser().getLocationInfo() + ": Caught and rethrew " + e.getClass().getName() + " - " + e.getMessage(),
				e );
		}
		catch( Error e ) {
			throw new SAXException( "Near " + getParser().getLocationInfo() + ": Caught and rethrew " + e.getClass().getName() + " - " + e.getMessage() );
		}
	}

	public void endElement(
		String		uri,
		String		localName,
		String		qName )
	throws SAXException
	{
	}
}
