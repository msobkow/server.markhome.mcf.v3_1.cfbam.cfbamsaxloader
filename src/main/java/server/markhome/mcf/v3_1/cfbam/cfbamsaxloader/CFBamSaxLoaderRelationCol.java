
// Description: Java 25 XML SAX Element Handler for RelationCol

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
 *	CFBamSaxLoaderRelationColParse XML SAX Element Handler implementation
 *	for RelationCol.
 */
public class CFBamSaxLoaderRelationCol
	extends CFLibXmlCoreElementHandler
{
	public CFBamSaxLoaderRelationCol( CFBamSaxLoader saxLoader ) {
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
		ICFBamRelationColObj origBuff = null;
		ICFBamRelationColEditObj editBuff = null;
		// Common XML Attributes
		String attrId = null;
		// RelationCol Attributes
		String attrName = null;
		String attrShortName = null;
		String attrLabel = null;
		String attrShortDescription = null;
		String attrDescription = null;
		String attrDefSchema = null;
		String attrFromCol = null;
		String attrToCol = null;
		// RelationCol References
		ICFBamRelationObj refRelation = null;
		ICFBamSchemaDefObj refDefSchema = null;
		ICFBamIndexColObj refFromCol = null;
		ICFBamIndexColObj refToCol = null;
		// Attribute Extraction
		String attrLocalName;
		int numAttrs;
		int idxAttr;
		final String S_LocalName = "LocalName";
		try {
			assert qName.equals( "RelationCol" );

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
			origBuff = (ICFBamRelationColObj)schemaObj.getRelationColTableObj().newInstance();
			editBuff = (ICFBamRelationColEditObj)origBuff.beginEdit();

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
				else if( attrLocalName.equals( "DefSchema" ) ) {
					if( attrDefSchema != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrDefSchema = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "FromCol" ) ) {
					if( attrFromCol != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrFromCol = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ToCol" ) ) {
					if( attrToCol != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrToCol = attrs.getValue( idxAttr );
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
			if( ( attrFromCol == null ) || ( attrFromCol.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"FromCol" );
			}
			if( ( attrToCol == null ) || ( attrToCol.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"ToCol" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "Name", attrName );
			curContext.putNamedValue( "ShortName", attrShortName );
			curContext.putNamedValue( "Label", attrLabel );
			curContext.putNamedValue( "ShortDescription", attrShortDescription );
			curContext.putNamedValue( "Description", attrDescription );
			curContext.putNamedValue( "DefSchema", attrDefSchema );
			curContext.putNamedValue( "FromCol", attrFromCol );
			curContext.putNamedValue( "ToCol", attrToCol );

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
			else if( scopeObj instanceof ICFBamRelationObj ) {
				refRelation = (ICFBamRelationObj) scopeObj;
				editBuff.setRequiredContainerRelation( refRelation );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFBamRelationObj" );
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

			// Lookup refFromCol by qualified name
			if( ( attrFromCol != null ) && ( attrFromCol.length() > 0 ) ) {
				refFromCol = (ICFBamIndexColObj)(editBuff.getNamedObject( schemaObj.getIndexColTableObj().getObjQualifyingClass(),
					attrFromCol ) );
				if( refFromCol == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve FromCol reference qualified name \"" + attrFromCol + "\" to table IndexCol" );
				}
			}
			else {
				refFromCol = null;
			}
			editBuff.setRequiredLookupFromCol( refFromCol );

			// Lookup refToCol by qualified name
			if( ( attrToCol != null ) && ( attrToCol.length() > 0 ) ) {
				refToCol = (ICFBamIndexColObj)(editBuff.getNamedObject( schemaObj.getIndexColTableObj().getObjQualifyingClass(),
					attrToCol ) );
				if( refToCol == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve ToCol reference qualified name \"" + attrToCol + "\" to table IndexCol" );
				}
			}
			else {
				refToCol = null;
			}
			editBuff.setRequiredLookupToCol( refToCol );

			CFBamSaxLoader.LoaderBehaviourEnum loaderBehaviour = saxLoader.getRelationColLoaderBehaviour();
			ICFBamRelationColEditObj editRelationCol = null;
			ICFBamRelationColObj origRelationCol = (ICFBamRelationColObj)schemaObj.getRelationColTableObj().readRelationColByUNameIdx( refRelation.getRequiredId(),
			editBuff.getRequiredName() );
			if( origRelationCol == null ) {
				editRelationCol = editBuff;
			}
			else {
				switch( loaderBehaviour ) {
					case Insert:
						break;
					case Update:
						editRelationCol = (ICFBamRelationColEditObj)origRelationCol.beginEdit();
						editRelationCol.setRequiredName( editBuff.getRequiredName() );
						editRelationCol.setOptionalShortName( editBuff.getOptionalShortName() );
						editRelationCol.setOptionalLabel( editBuff.getOptionalLabel() );
						editRelationCol.setOptionalShortDescription( editBuff.getOptionalShortDescription() );
						editRelationCol.setOptionalDescription( editBuff.getOptionalDescription() );
						editRelationCol.setOptionalLookupDefSchema( editBuff.getOptionalLookupDefSchema() );
						editRelationCol.setRequiredLookupFromCol( editBuff.getRequiredLookupFromCol() );
						editRelationCol.setRequiredLookupToCol( editBuff.getRequiredLookupToCol() );
						break;
					case Replace:
						editRelationCol = (ICFBamRelationColEditObj)origRelationCol.beginEdit();
						editRelationCol.deleteInstance();
						editRelationCol = null;
						origRelationCol = null;
						editRelationCol = editBuff;
						break;
				}
			}

			if( editRelationCol != null ) {
				if( origRelationCol != null ) {
					editRelationCol.update();
				}
				else {
					origRelationCol = (ICFBamRelationColObj)editRelationCol.create();
				}
				editRelationCol = null;
			}

			curContext.putNamedValue( "Object", origRelationCol );
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
