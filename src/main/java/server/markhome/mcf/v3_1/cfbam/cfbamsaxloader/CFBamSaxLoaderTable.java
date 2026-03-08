
// Description: Java 25 XML SAX Element Handler for Table

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
 *	CFBamSaxLoaderTableParse XML SAX Element Handler implementation
 *	for Table.
 */
public class CFBamSaxLoaderTable
	extends CFLibXmlCoreElementHandler
{
	public CFBamSaxLoaderTable( CFBamSaxLoader saxLoader ) {
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
		ICFBamTableObj origBuff = null;
		ICFBamTableEditObj editBuff = null;
		// Common XML Attributes
		String attrId = null;
		// Scope Attributes
		// Scope References
		ICFBamTenantObj refTenant = null;
		// Table Attributes
		String attrName = null;
		String attrDbName = null;
		String attrShortName = null;
		String attrLabel = null;
		String attrShortDescription = null;
		String attrDescription = null;
		String attrPageData = null;
		String attrTableClassCode = null;
		String attrIsInstantiable = null;
		String attrHasHistory = null;
		String attrHasAuditColumns = null;
		String attrIsMutable = null;
		String attrIsServerOnly = null;
		String attrLoaderBehaviour = null;
		String attrSecScope = null;
		String attrDefSchema = null;
		String attrLookupIndex = null;
		String attrAltIndex = null;
		String attrQualTable = null;
		String attrPrimaryIndex = null;
		// Table References
		ICFBamSchemaDefObj refSchemaDef = null;
		ICFBamSchemaDefObj refDefSchema = null;
		ICFBamIndexObj refLookupIndex = null;
		ICFBamIndexObj refAltIndex = null;
		ICFBamTableObj refQualTable = null;
		ICFBamIndexObj refPrimaryIndex = null;
		// Attribute Extraction
		String attrLocalName;
		int numAttrs;
		int idxAttr;
		final String S_LocalName = "LocalName";
		try {
			assert qName.equals( "Table" );

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
			origBuff = (ICFBamTableObj)schemaObj.getTableTableObj().newInstance();
			editBuff = (ICFBamTableEditObj)origBuff.beginEdit();

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
				else if( attrLocalName.equals( "PageData" ) ) {
					if( attrPageData != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrPageData = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "TableClassCode" ) ) {
					if( attrTableClassCode != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrTableClassCode = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "IsInstantiable" ) ) {
					if( attrIsInstantiable != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrIsInstantiable = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "HasHistory" ) ) {
					if( attrHasHistory != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrHasHistory = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "HasAuditColumns" ) ) {
					if( attrHasAuditColumns != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrHasAuditColumns = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "IsMutable" ) ) {
					if( attrIsMutable != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrIsMutable = attrs.getValue( idxAttr );
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
				else if( attrLocalName.equals( "LoaderBehaviour" ) ) {
					if( attrLoaderBehaviour != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrLoaderBehaviour = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "SecScope" ) ) {
					if( attrSecScope != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrSecScope = attrs.getValue( idxAttr );
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
				else if( attrLocalName.equals( "LookupIndex" ) ) {
					if( attrLookupIndex != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrLookupIndex = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "AltIndex" ) ) {
					if( attrAltIndex != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrAltIndex = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "QualTable" ) ) {
					if( attrQualTable != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrQualTable = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "PrimaryIndex" ) ) {
					if( attrPrimaryIndex != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrPrimaryIndex = attrs.getValue( idxAttr );
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
			if( ( attrPageData == null ) || ( attrPageData.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"PageData" );
			}
			if( attrTableClassCode == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"TableClassCode" );
			}
			if( ( attrIsInstantiable == null ) || ( attrIsInstantiable.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"IsInstantiable" );
			}
			if( ( attrHasHistory == null ) || ( attrHasHistory.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"HasHistory" );
			}
			if( ( attrHasAuditColumns == null ) || ( attrHasAuditColumns.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"HasAuditColumns" );
			}
			if( ( attrIsMutable == null ) || ( attrIsMutable.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"IsMutable" );
			}
			if( ( attrIsServerOnly == null ) || ( attrIsServerOnly.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"IsServerOnly" );
			}
			if( ( attrLoaderBehaviour == null ) || ( attrLoaderBehaviour.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"LoaderBehaviour" );
			}
			if( ( attrSecScope == null ) || ( attrSecScope.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"SecScope" );
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
			curContext.putNamedValue( "PageData", attrPageData );
			curContext.putNamedValue( "TableClassCode", attrTableClassCode );
			curContext.putNamedValue( "IsInstantiable", attrIsInstantiable );
			curContext.putNamedValue( "HasHistory", attrHasHistory );
			curContext.putNamedValue( "HasAuditColumns", attrHasAuditColumns );
			curContext.putNamedValue( "IsMutable", attrIsMutable );
			curContext.putNamedValue( "IsServerOnly", attrIsServerOnly );
			curContext.putNamedValue( "LoaderBehaviour", attrLoaderBehaviour );
			curContext.putNamedValue( "SecScope", attrSecScope );
			curContext.putNamedValue( "DefSchema", attrDefSchema );
			curContext.putNamedValue( "LookupIndex", attrLookupIndex );
			curContext.putNamedValue( "AltIndex", attrAltIndex );
			curContext.putNamedValue( "QualTable", attrQualTable );
			curContext.putNamedValue( "PrimaryIndex", attrPrimaryIndex );

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

			boolean natPageData;
			if( attrPageData.equals( "true" ) || attrPageData.equals( "yes" ) || attrPageData.equals( "1" ) ) {
				natPageData = true;
			}
			else if( attrPageData.equals( "false" ) || attrPageData.equals( "no" ) || attrPageData.equals( "0" ) ) {
				natPageData = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "PageData", attrPageData),
					String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "PageData", attrPageData));
			}
			editBuff.setRequiredPageData( natPageData );

			String natTableClassCode = attrTableClassCode;
			editBuff.setRequiredTableClassCode( natTableClassCode );

			boolean natIsInstantiable;
			if( attrIsInstantiable.equals( "true" ) || attrIsInstantiable.equals( "yes" ) || attrIsInstantiable.equals( "1" ) ) {
				natIsInstantiable = true;
			}
			else if( attrIsInstantiable.equals( "false" ) || attrIsInstantiable.equals( "no" ) || attrIsInstantiable.equals( "0" ) ) {
				natIsInstantiable = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "IsInstantiable", attrIsInstantiable),
					String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "IsInstantiable", attrIsInstantiable));
			}
			editBuff.setRequiredIsInstantiable( natIsInstantiable );

			boolean natHasHistory;
			if( attrHasHistory.equals( "true" ) || attrHasHistory.equals( "yes" ) || attrHasHistory.equals( "1" ) ) {
				natHasHistory = true;
			}
			else if( attrHasHistory.equals( "false" ) || attrHasHistory.equals( "no" ) || attrHasHistory.equals( "0" ) ) {
				natHasHistory = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "HasHistory", attrHasHistory),
					String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "HasHistory", attrHasHistory));
			}
			editBuff.setRequiredHasHistory( natHasHistory );

			boolean natHasAuditColumns;
			if( attrHasAuditColumns.equals( "true" ) || attrHasAuditColumns.equals( "yes" ) || attrHasAuditColumns.equals( "1" ) ) {
				natHasAuditColumns = true;
			}
			else if( attrHasAuditColumns.equals( "false" ) || attrHasAuditColumns.equals( "no" ) || attrHasAuditColumns.equals( "0" ) ) {
				natHasAuditColumns = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "HasAuditColumns", attrHasAuditColumns),
					String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "HasAuditColumns", attrHasAuditColumns));
			}
			editBuff.setRequiredHasAuditColumns( natHasAuditColumns );

			boolean natIsMutable;
			if( attrIsMutable.equals( "true" ) || attrIsMutable.equals( "yes" ) || attrIsMutable.equals( "1" ) ) {
				natIsMutable = true;
			}
			else if( attrIsMutable.equals( "false" ) || attrIsMutable.equals( "no" ) || attrIsMutable.equals( "0" ) ) {
				natIsMutable = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "IsMutable", attrIsMutable),
					String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "IsMutable", attrIsMutable));
			}
			editBuff.setRequiredIsMutable( natIsMutable );

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

			ICFBamSchema.LoaderBehaviourEnum natLoaderBehaviour = ICFBamSchema.parseLoaderBehaviourEnum( attrLoaderBehaviour );
			editBuff.setRequiredLoaderBehaviour( natLoaderBehaviour );

			ICFBamSchema.SecScopeEnum natSecScope = ICFBamSchema.parseSecScopeEnum( attrSecScope );
			editBuff.setRequiredSecScope( natSecScope );

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
				refTenant = (ICFBamTenantObj)editBuff.getRequiredOwnerTenant();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFBamSchemaDefObj" );
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

			// Lookup refQualTable by qualified name
			if( ( attrQualTable != null ) && ( attrQualTable.length() > 0 ) ) {
				refQualTable = (ICFBamTableObj)(editBuff.getNamedObject( schemaObj.getTableTableObj().getObjQualifyingClass(),
					attrQualTable ) );
				if( refQualTable == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve QualTable reference qualified name \"" + attrQualTable + "\" to table Table" );
				}
			}
			else {
				refQualTable = null;
			}
			editBuff.setOptionalLookupQualTable( refQualTable );

			CFBamSaxLoader.LoaderBehaviourEnum loaderBehaviour = saxLoader.getTableLoaderBehaviour();
			ICFBamTableEditObj editTable = null;
			ICFBamTableObj origTable = (ICFBamTableObj)schemaObj.getTableTableObj().readTableByUNameIdx( refSchemaDef.getRequiredId(),
			editBuff.getRequiredName() );
			if( origTable == null ) {
				editTable = editBuff;
			}
			else {
				switch( loaderBehaviour ) {
					case Insert:
						break;
					case Update:
						editTable = (ICFBamTableEditObj)origTable.beginEdit();
						editTable.setRequiredName( editBuff.getRequiredName() );
						editTable.setOptionalDbName( editBuff.getOptionalDbName() );
						editTable.setOptionalShortName( editBuff.getOptionalShortName() );
						editTable.setOptionalLabel( editBuff.getOptionalLabel() );
						editTable.setOptionalShortDescription( editBuff.getOptionalShortDescription() );
						editTable.setOptionalDescription( editBuff.getOptionalDescription() );
						editTable.setRequiredPageData( editBuff.getRequiredPageData() );
						editTable.setRequiredTableClassCode( editBuff.getRequiredTableClassCode() );
						editTable.setRequiredIsInstantiable( editBuff.getRequiredIsInstantiable() );
						editTable.setRequiredHasHistory( editBuff.getRequiredHasHistory() );
						editTable.setRequiredHasAuditColumns( editBuff.getRequiredHasAuditColumns() );
						editTable.setRequiredIsMutable( editBuff.getRequiredIsMutable() );
						editTable.setRequiredIsServerOnly( editBuff.getRequiredIsServerOnly() );
						editTable.setRequiredLoaderBehaviour( editBuff.getRequiredLoaderBehaviour() );
						editTable.setRequiredSecScope( editBuff.getRequiredSecScope() );
						editTable.setOptionalLookupDefSchema( editBuff.getOptionalLookupDefSchema() );
						editTable.setOptionalLookupLookupIndex( editBuff.getOptionalLookupLookupIndex() );
						editTable.setOptionalLookupAltIndex( editBuff.getOptionalLookupAltIndex() );
						editTable.setOptionalLookupQualTable( editBuff.getOptionalLookupQualTable() );
						editTable.setOptionalLookupPrimaryIndex( editBuff.getOptionalLookupPrimaryIndex() );
						break;
					case Replace:
						editTable = (ICFBamTableEditObj)origTable.beginEdit();
						editTable.deleteInstance();
						editTable = null;
						origTable = null;
						editTable = editBuff;
						break;
				}
			}

			if( editTable != null ) {
				if( origTable != null ) {
					editTable.update();
				}
				else {
					origTable = (ICFBamTableObj)editTable.create();
				}
				editTable = null;
			}

			curContext.putNamedValue( "Object", origTable );
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
		final String S_ProcName = "endElement";
		ICFBamTableObj origBuff = null;
		ICFBamTableEditObj editBuff = null;
		String attrLookupIndex = null;
		String attrAltIndex = null;
		String attrPrimaryIndex = null;
		ICFBamIndexObj refLookupIndex = null;
		ICFBamIndexObj refAltIndex = null;
		ICFBamIndexObj refPrimaryIndex = null;
		try {
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

			CFLibXmlCoreContext curContext = getParser().getCurContext();
			if( curContext == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"curContext" );
			}

			Object obj = curContext.getNamedValue( "Object" );
			if( obj == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"obj" );
			}
			else if( obj instanceof ICFBamTableObj ) {
				origBuff = (ICFBamTableObj)obj;
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"obj",
					obj,
					"ICFBamTableObj" );
			}

			attrLookupIndex = (String)curContext.getNamedValue( "LookupIndex" );
			attrAltIndex = (String)curContext.getNamedValue( "AltIndex" );
			attrPrimaryIndex = (String)curContext.getNamedValue( "PrimaryIndex" );

			// Lookup refLookupIndex by qualified name
			if( ( attrLookupIndex != null ) && ( attrLookupIndex.length() > 0 ) ) {
				refLookupIndex = (ICFBamIndexObj)( origBuff.getNamedObject( schemaObj.getIndexTableObj().getObjQualifyingClass(),
					attrLookupIndex ) );
				if( refLookupIndex == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve LookupIndex reference qualified name \"" + attrLookupIndex + "\" to table Index" );
				}
			}
			else {
				refLookupIndex = null;
			}
			if( origBuff.getOptionalLookupLookupIndex() != refLookupIndex ) {
				if( editBuff == null ) {
					editBuff = (ICFBamTableEditObj)( origBuff.getEdit() );
					if( editBuff == null ) {
						editBuff = (ICFBamTableEditObj)( origBuff.beginEdit() );
					}
				}
				editBuff.setOptionalLookupLookupIndex( refLookupIndex );
			}

			// Lookup refAltIndex by qualified name
			if( ( attrAltIndex != null ) && ( attrAltIndex.length() > 0 ) ) {
				refAltIndex = (ICFBamIndexObj)( origBuff.getNamedObject( schemaObj.getIndexTableObj().getObjQualifyingClass(),
					attrAltIndex ) );
				if( refAltIndex == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve AltIndex reference qualified name \"" + attrAltIndex + "\" to table Index" );
				}
			}
			else {
				refAltIndex = null;
			}
			if( origBuff.getOptionalLookupAltIndex() != refAltIndex ) {
				if( editBuff == null ) {
					editBuff = (ICFBamTableEditObj)( origBuff.getEdit() );
					if( editBuff == null ) {
						editBuff = (ICFBamTableEditObj)( origBuff.beginEdit() );
					}
				}
				editBuff.setOptionalLookupAltIndex( refAltIndex );
			}

			// Lookup refPrimaryIndex by qualified name
			if( ( attrPrimaryIndex != null ) && ( attrPrimaryIndex.length() > 0 ) ) {
				refPrimaryIndex = (ICFBamIndexObj)( origBuff.getNamedObject( schemaObj.getIndexTableObj().getObjQualifyingClass(),
					attrPrimaryIndex ) );
				if( refPrimaryIndex == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve PrimaryIndex reference qualified name \"" + attrPrimaryIndex + "\" to table Index" );
				}
			}
			else {
				refPrimaryIndex = null;
			}
			if( origBuff.getOptionalLookupPrimaryIndex() != refPrimaryIndex ) {
				if( editBuff == null ) {
					editBuff = (ICFBamTableEditObj)( origBuff.getEdit() );
					if( editBuff == null ) {
						editBuff = (ICFBamTableEditObj)( origBuff.beginEdit() );
					}
				}
				editBuff.setOptionalLookupPrimaryIndex( refPrimaryIndex );
			}

			if( editBuff != null ) {
				editBuff.update();
				editBuff = null;
			}
		}
		catch( RuntimeException e ) {
			if( editBuff != null ) {
				editBuff.endEdit();
				editBuff = null;
			}
			throw new SAXException( "Near " + getParser().getLocationInfo() + ": Caught and rethrew " + e.getClass().getName() + " - " + e.getMessage(),
				e );
		}
		catch( Error e ) {
			if( editBuff != null ) {
				editBuff.endEdit();
				editBuff = null;
			}
			throw new SAXException( "Near " + getParser().getLocationInfo() + ": Caught and rethrew " + e.getClass().getName() + " - " + e.getMessage() );
		}
		if( editBuff != null ) {
			editBuff.endEdit();
			editBuff = null;
		}
	}
}
