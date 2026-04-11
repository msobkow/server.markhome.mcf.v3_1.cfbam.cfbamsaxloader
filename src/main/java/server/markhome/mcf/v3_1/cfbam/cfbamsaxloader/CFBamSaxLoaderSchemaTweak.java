
// Description: Java 25 XML SAX Element Handler for SchemaTweak

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
 *	CFBamSaxLoaderSchemaTweakParse XML SAX Element Handler implementation
 *	for SchemaTweak.
 */
public class CFBamSaxLoaderSchemaTweak
	extends CFLibXmlCoreElementHandler
{
	public CFBamSaxLoaderSchemaTweak( CFBamSaxLoader saxLoader ) {
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
		ICFBamSchemaTweakObj origBuff = null;
		ICFBamSchemaTweakEditObj editBuff = null;
		// Common XML Attributes
		String attrId = null;
		// Tweak Attributes
		String attrTenantId = null;
		String attrDefSchemaTenantId = null;
		String attrName = null;
		String attrReplacesInherited = null;
		String attrTweakGelText = null;
		String attrDefSchema = null;
		// Tweak References
		ICFBamScopeObj refScopeDef = null;
		ICFBamSchemaDefObj refDefSchema = null;
		// SchemaTweak Attributes
		// SchemaTweak References
		ICFBamSchemaDefObj refSchemaDef = null;
		// Attribute Extraction
		String attrLocalName;
		int numAttrs;
		int idxAttr;
		final String S_LocalName = "LocalName";
		try {
			assert qName.equals( "SchemaTweak" );

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
			origBuff = (ICFBamSchemaTweakObj)schemaObj.getSchemaTweakTableObj().newInstance();
			editBuff = (ICFBamSchemaTweakEditObj)origBuff.beginEdit();

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
				else if( attrLocalName.equals( "TenantId" ) ) {
					if( attrTenantId != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrTenantId = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "DefSchemaTenantId" ) ) {
					if( attrDefSchemaTenantId != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrDefSchemaTenantId = attrs.getValue( idxAttr );
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
				else if( attrLocalName.equals( "ReplacesInherited" ) ) {
					if( attrReplacesInherited != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrReplacesInherited = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "TweakGelText" ) ) {
					if( attrTweakGelText != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrTweakGelText = attrs.getValue( idxAttr );
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
			if( ( attrTenantId == null ) || ( attrTenantId.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"TenantId" );
			}
			if( attrName == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"Name" );
			}
			if( ( attrReplacesInherited == null ) || ( attrReplacesInherited.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"ReplacesInherited" );
			}
			if( attrTweakGelText == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"TweakGelText" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "TenantId", attrTenantId );
			curContext.putNamedValue( "DefSchemaTenantId", attrDefSchemaTenantId );
			curContext.putNamedValue( "Name", attrName );
			curContext.putNamedValue( "ReplacesInherited", attrReplacesInherited );
			curContext.putNamedValue( "TweakGelText", attrTweakGelText );
			curContext.putNamedValue( "DefSchema", attrDefSchema );

			// Convert string attributes to native Java types
			// and apply the converted attributes to the editBuff.

			Integer natId;
			if( ( attrId != null ) && ( attrId.length() > 0 ) ) {
				natId = Integer.valueOf( Integer.parseInt( attrId ) );
			}
			else {
				natId = null;
			}
			CFLibDbKeyHash256 natTenantId;
			try {
				natTenantId = CFLibDbKeyHash256.fromHex( attrTenantId );
			}
			catch( RuntimeException e ) {
				throw new CFLibInvalidArgumentException( getClass(),
					S_ProcName,
					0,
					"TenantId",
					e );
			}
			editBuff.setRequiredTenantId( natTenantId );

			CFLibDbKeyHash256 natDefSchemaTenantId;
			if( ( attrDefSchemaTenantId == null ) || ( attrDefSchemaTenantId.length() <= 0 ) ) {
				natDefSchemaTenantId = null;
			}
			else {
				try {
					natDefSchemaTenantId = CFLibDbKeyHash256.fromHex( attrDefSchemaTenantId );
				}
				catch( RuntimeException e ) {
					throw new CFLibInvalidArgumentException( getClass(),
						S_ProcName,
						0,
						"DefSchemaTenantId",
						e );
				}
			}
			editBuff.setOptionalDefSchemaTenantId( natDefSchemaTenantId );

			String natName = attrName;
			editBuff.setRequiredName( natName );

			boolean natReplacesInherited;
			if( attrReplacesInherited.equals( "true" ) || attrReplacesInherited.equals( "yes" ) || attrReplacesInherited.equals( "1" ) ) {
				natReplacesInherited = true;
			}
			else if( attrReplacesInherited.equals( "false" ) || attrReplacesInherited.equals( "no" ) || attrReplacesInherited.equals( "0" ) ) {
				natReplacesInherited = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "ReplacesInherited", attrReplacesInherited),
					String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "ReplacesInherited", attrReplacesInherited));
			}
			editBuff.setRequiredReplacesInherited( natReplacesInherited );

			String natTweakGelText = attrTweakGelText;
			editBuff.setRequiredTweakGelText( natTweakGelText );

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
			else if( scopeObj instanceof ICFBamSchemaDefObj ) {
				refSchemaDef = (ICFBamSchemaDefObj) scopeObj;
				editBuff.setRequiredContainerSchemaDef( refSchemaDef );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFBamSchemaDefObj" );
			}

			refScopeDef = refSchemaDef;
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

			CFBamSaxLoader.LoaderBehaviourEnum loaderBehaviour = saxLoader.getSchemaTweakLoaderBehaviour();
			ICFBamSchemaTweakEditObj editSchemaTweak = null;
			ICFBamSchemaTweakObj origSchemaTweak = (ICFBamSchemaTweakObj)schemaObj.getSchemaTweakTableObj().readSchemaTweakByUNameIdx( refScopeDef.getRequiredId(),
			editBuff.getRequiredName() );
			if( origSchemaTweak == null ) {
				editSchemaTweak = editBuff;
			}
			else {
				switch( loaderBehaviour ) {
					case Insert:
						break;
					case Update:
						editSchemaTweak = (ICFBamSchemaTweakEditObj)origSchemaTweak.beginEdit();
						editSchemaTweak.setRequiredTenantId( editBuff.getRequiredTenantId() );
						editSchemaTweak.setOptionalDefSchemaTenantId( editBuff.getOptionalDefSchemaTenantId() );
						editSchemaTweak.setRequiredName( editBuff.getRequiredName() );
						editSchemaTweak.setRequiredReplacesInherited( editBuff.getRequiredReplacesInherited() );
						editSchemaTweak.setRequiredTweakGelText( editBuff.getRequiredTweakGelText() );
						editSchemaTweak.setOptionalLookupDefSchema( editBuff.getOptionalLookupDefSchema() );
						break;
					case Replace:
						editSchemaTweak = (ICFBamSchemaTweakEditObj)origSchemaTweak.beginEdit();
						editSchemaTweak.deleteInstance();
						editSchemaTweak = null;
						origSchemaTweak = null;
						editSchemaTweak = editBuff;
						break;
				}
			}

			if( editSchemaTweak != null ) {
				if( origSchemaTweak != null ) {
					editSchemaTweak.update();
				}
				else {
					origSchemaTweak = (ICFBamSchemaTweakObj)editSchemaTweak.create();
				}
				editSchemaTweak = null;
			}

			curContext.putNamedValue( "Object", origSchemaTweak );
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
