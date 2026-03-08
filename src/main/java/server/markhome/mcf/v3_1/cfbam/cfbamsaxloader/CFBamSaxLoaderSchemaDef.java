
// Description: Java 25 XML SAX Element Handler for SchemaDef

/*
 *	server.markhome.mcf.CFBam
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal 3.1 CFBam - Business Application Model
 *	
 *	This file is part of Mark's Code Fractal CFBam.
 *	
 *	Mark's Code Fractal CFBam is available under dual commercial license from
 *	Mark Stephen Sobkow, or under the terms of the GNU General Public License,
 *	Version 3 or later.
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
 *	
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
 *	CFBamSaxLoaderSchemaDefParse XML SAX Element Handler implementation
 *	for SchemaDef.
 */
public class CFBamSaxLoaderSchemaDef
	extends CFLibXmlCoreElementHandler
{
	public CFBamSaxLoaderSchemaDef( CFBamSaxLoader saxLoader ) {
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
		ICFBamSchemaDefObj origBuff = null;
		ICFBamSchemaDefEditObj editBuff = null;
		// Common XML Attributes
		String attrId = null;
		// Scope Attributes
		// Scope References
		ICFBamTenantObj refTenant = null;
		// SchemaDef Attributes
		String attrName = null;
		String attrDbName = null;
		String attrShortName = null;
		String attrLabel = null;
		String attrShortDescription = null;
		String attrDescription = null;
		String attrCopyrightPeriod = null;
		String attrCopyrightHolder = null;
		String attrAuthorEMail = null;
		String attrProjectURL = null;
		String attrPublishURI = null;
		// SchemaDef References
		ICFBamMinorVersionObj refMinorVersion = null;
		ICFBamTenantObj refCTenant = null;
		// Attribute Extraction
		String attrLocalName;
		int numAttrs;
		int idxAttr;
		final String S_LocalName = "LocalName";
		try {
			assert qName.equals( "SchemaDef" );

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
			origBuff = (ICFBamSchemaDefObj)schemaObj.getSchemaDefTableObj().newInstance();
			editBuff = (ICFBamSchemaDefEditObj)origBuff.beginEdit();

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
				else if( attrLocalName.equals( "DbName" ) ) {
					if( attrDbName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrDbName = attrs.getValue( idxAttr );
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
				else if( attrLocalName.equals( "CopyrightPeriod" ) ) {
					if( attrCopyrightPeriod != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrCopyrightPeriod = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "CopyrightHolder" ) ) {
					if( attrCopyrightHolder != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrCopyrightHolder = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "AuthorEMail" ) ) {
					if( attrAuthorEMail != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrAuthorEMail = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ProjectURL" ) ) {
					if( attrProjectURL != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrProjectURL = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "PublishURI" ) ) {
					if( attrPublishURI != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrPublishURI = attrs.getValue( idxAttr );
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
			if( attrCopyrightPeriod == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"CopyrightPeriod" );
			}
			if( attrCopyrightHolder == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"CopyrightHolder" );
			}
			if( attrAuthorEMail == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"AuthorEMail" );
			}
			if( attrProjectURL == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"ProjectURL" );
			}
			if( attrPublishURI == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"PublishURI" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "Name", attrName );
			curContext.putNamedValue( "DbName", attrDbName );
			curContext.putNamedValue( "ShortName", attrShortName );
			curContext.putNamedValue( "Label", attrLabel );
			curContext.putNamedValue( "ShortDescription", attrShortDescription );
			curContext.putNamedValue( "Description", attrDescription );
			curContext.putNamedValue( "CopyrightPeriod", attrCopyrightPeriod );
			curContext.putNamedValue( "CopyrightHolder", attrCopyrightHolder );
			curContext.putNamedValue( "AuthorEMail", attrAuthorEMail );
			curContext.putNamedValue( "ProjectURL", attrProjectURL );
			curContext.putNamedValue( "PublishURI", attrPublishURI );

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

			String natDbName = attrDbName;
			editBuff.setOptionalDbName( natDbName );

			String natShortName = attrShortName;
			editBuff.setOptionalShortName( natShortName );

			String natLabel = attrLabel;
			editBuff.setOptionalLabel( natLabel );

			String natShortDescription = attrShortDescription;
			editBuff.setOptionalShortDescription( natShortDescription );

			String natDescription = attrDescription;
			editBuff.setOptionalDescription( natDescription );

			String natCopyrightPeriod = attrCopyrightPeriod;
			editBuff.setRequiredCopyrightPeriod( natCopyrightPeriod );

			String natCopyrightHolder = attrCopyrightHolder;
			editBuff.setRequiredCopyrightHolder( natCopyrightHolder );

			String natAuthorEMail = attrAuthorEMail;
			editBuff.setRequiredAuthorEMail( natAuthorEMail );

			String natProjectURL = attrProjectURL;
			editBuff.setRequiredProjectURL( natProjectURL );

			String natPublishURI = attrPublishURI;
			editBuff.setRequiredPublishURI( natPublishURI );

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
			else if( scopeObj instanceof ICFBamMinorVersionObj ) {
				refMinorVersion = (ICFBamMinorVersionObj) scopeObj;
				editBuff.setRequiredContainerMinorVersion( refMinorVersion );
				refCTenant = (ICFBamTenantObj)editBuff.getRequiredOwnerCTenant();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFBamMinorVersionObj" );
			}

			// Resolve and apply Owner reference

			if( refCTenant == null ) {
				if( scopeObj instanceof ICFBamTenantObj ) {
					refCTenant = (ICFBamTenantObj) scopeObj;
					editBuff.setRequiredOwnerCTenant( refCTenant );
				}
				else {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Owner<CTenant>" );
				}
			}

			refTenant = refCTenant;
			CFBamSaxLoader.LoaderBehaviourEnum loaderBehaviour = saxLoader.getSchemaDefLoaderBehaviour();
			ICFBamSchemaDefEditObj editSchemaDef = null;
			ICFBamSchemaDefObj origSchemaDef = (ICFBamSchemaDefObj)schemaObj.getSchemaDefTableObj().readSchemaDefByUNameIdx( refMinorVersion.getRequiredId(),
			editBuff.getRequiredName() );
			if( origSchemaDef == null ) {
				editSchemaDef = editBuff;
			}
			else {
				switch( loaderBehaviour ) {
					case Insert:
						break;
					case Update:
						editSchemaDef = (ICFBamSchemaDefEditObj)origSchemaDef.beginEdit();
						editSchemaDef.setRequiredName( editBuff.getRequiredName() );
						editSchemaDef.setOptionalDbName( editBuff.getOptionalDbName() );
						editSchemaDef.setOptionalShortName( editBuff.getOptionalShortName() );
						editSchemaDef.setOptionalLabel( editBuff.getOptionalLabel() );
						editSchemaDef.setOptionalShortDescription( editBuff.getOptionalShortDescription() );
						editSchemaDef.setOptionalDescription( editBuff.getOptionalDescription() );
						editSchemaDef.setRequiredCopyrightPeriod( editBuff.getRequiredCopyrightPeriod() );
						editSchemaDef.setRequiredCopyrightHolder( editBuff.getRequiredCopyrightHolder() );
						editSchemaDef.setRequiredAuthorEMail( editBuff.getRequiredAuthorEMail() );
						editSchemaDef.setRequiredProjectURL( editBuff.getRequiredProjectURL() );
						editSchemaDef.setRequiredPublishURI( editBuff.getRequiredPublishURI() );
						break;
					case Replace:
						editSchemaDef = (ICFBamSchemaDefEditObj)origSchemaDef.beginEdit();
						editSchemaDef.deleteInstance();
						editSchemaDef = null;
						origSchemaDef = null;
						editSchemaDef = editBuff;
						break;
				}
			}

			if( editSchemaDef != null ) {
				if( origSchemaDef != null ) {
					editSchemaDef.update();
				}
				else {
					origSchemaDef = (ICFBamSchemaDefObj)editSchemaDef.create();
				}
				editSchemaDef = null;
			}

			curContext.putNamedValue( "Object", origSchemaDef );
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
