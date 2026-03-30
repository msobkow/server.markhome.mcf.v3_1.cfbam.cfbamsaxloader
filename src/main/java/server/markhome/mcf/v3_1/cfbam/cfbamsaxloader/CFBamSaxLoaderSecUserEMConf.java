
// Description: Java 25 XML SAX Element Handler for SecUserEMConf

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
 *	CFBamSaxLoaderSecUserEMConfParse XML SAX Element Handler implementation
 *	for SecUserEMConf.
 */
public class CFBamSaxLoaderSecUserEMConf
	extends CFLibXmlCoreElementHandler
{
	public CFBamSaxLoaderSecUserEMConf( CFBamSaxLoader saxLoader ) {
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
		ICFBamSecUserEMConfObj origBuff = null;
		ICFBamSecUserEMConfEditObj editBuff = null;
		// Common XML Attributes
		String attrId = null;
		// SecUserEMConf Attributes
		String attrConfirmEMailAddr = null;
		String attrEMailSentStamp = null;
		String attrEMConfirmationUuid6 = null;
		String attrNewAccount = null;
		// SecUserEMConf References
		ICFBamSecUserObj refUser = null;
		// Attribute Extraction
		String attrLocalName;
		int numAttrs;
		int idxAttr;
		final String S_LocalName = "LocalName";
		try {
			assert qName.equals( "SecUserEMConf" );

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
			origBuff = (ICFBamSecUserEMConfObj)schemaObj.getSecUserEMConfTableObj().newInstance();
			editBuff = (ICFBamSecUserEMConfEditObj)origBuff.beginEdit();

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
				else if( attrLocalName.equals( "ConfirmEMailAddr" ) ) {
					if( attrConfirmEMailAddr != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrConfirmEMailAddr = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "EMailSentStamp" ) ) {
					if( attrEMailSentStamp != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrEMailSentStamp = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "EMConfirmationUuid6" ) ) {
					if( attrEMConfirmationUuid6 != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrEMConfirmationUuid6 = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "NewAccount" ) ) {
					if( attrNewAccount != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrNewAccount = attrs.getValue( idxAttr );
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
			if( attrConfirmEMailAddr == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"ConfirmEMailAddr" );
			}
			if( ( attrEMailSentStamp == null ) || ( attrEMailSentStamp.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"EMailSentStamp" );
			}
			if( ( attrEMConfirmationUuid6 == null ) || ( attrEMConfirmationUuid6.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"EMConfirmationUuid6" );
			}
			if( ( attrNewAccount == null ) || ( attrNewAccount.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"NewAccount" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "ConfirmEMailAddr", attrConfirmEMailAddr );
			curContext.putNamedValue( "EMailSentStamp", attrEMailSentStamp );
			curContext.putNamedValue( "EMConfirmationUuid6", attrEMConfirmationUuid6 );
			curContext.putNamedValue( "NewAccount", attrNewAccount );

			// Convert string attributes to native Java types
			// and apply the converted attributes to the editBuff.

			Integer natId;
			if( ( attrId != null ) && ( attrId.length() > 0 ) ) {
				natId = Integer.valueOf( Integer.parseInt( attrId ) );
			}
			else {
				natId = null;
			}
			String natConfirmEMailAddr = attrConfirmEMailAddr;
			editBuff.setRequiredConfirmEMailAddr( natConfirmEMailAddr );

			LocalDateTime natEMailSentStamp;
			try {
				natEMailSentStamp = CFLibXmlUtil.parseTimestamp( attrEMailSentStamp );
			}
			catch( RuntimeException e ) {
				throw new CFLibInvalidArgumentException( getClass(),
					S_ProcName,
					0,
					"EMailSentStamp",
					e );
			}
			editBuff.setRequiredEMailSentStamp( natEMailSentStamp );

			CFLibUuid6 natEMConfirmationUuid6;
			try {
				natEMConfirmationUuid6 = CFLibUuid6.fromString( attrEMConfirmationUuid6 );
			}
			catch( RuntimeException e ) {
				throw new CFLibInvalidArgumentException( getClass(),
					S_ProcName,
					0,
					"EMConfirmationUuid6",
					e );
			}
			editBuff.setRequiredEMConfirmationUuid6( natEMConfirmationUuid6 );

			boolean natNewAccount;
			if( attrNewAccount.equals( "true" ) || attrNewAccount.equals( "yes" ) || attrNewAccount.equals( "1" ) ) {
				natNewAccount = true;
			}
			else if( attrNewAccount.equals( "false" ) || attrNewAccount.equals( "no" ) || attrNewAccount.equals( "0" ) ) {
				natNewAccount = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					String.format(Inz.x("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "NewAccount", attrNewAccount),
					String.format(Inz.s("cflib.xml.CFLibXmlUtil.XmlBooleanInvalid"), "NewAccount", attrNewAccount));
			}
			editBuff.setRequiredNewAccount( natNewAccount );

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
			else if( scopeObj instanceof ICFBamSecUserObj ) {
				refUser = (ICFBamSecUserObj) scopeObj;
				editBuff.setRequiredContainerUser( refUser );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFBamSecUserObj" );
			}

			CFBamSaxLoader.LoaderBehaviourEnum loaderBehaviour = saxLoader.getSecUserEMConfLoaderBehaviour();
			ICFBamSecUserEMConfEditObj editSecUserEMConf = null;
			ICFBamSecUserEMConfObj origSecUserEMConf = (ICFBamSecUserEMConfObj)schemaObj.getSecUserEMConfTableObj().readSecUserEMConfByUUuid6Idx( editBuff.getRequiredEMConfirmationUuid6() );
			if( origSecUserEMConf == null ) {
				editSecUserEMConf = editBuff;
			}
			else {
				switch( loaderBehaviour ) {
					case Insert:
						break;
					case Update:
						editSecUserEMConf = (ICFBamSecUserEMConfEditObj)origSecUserEMConf.beginEdit();
						editSecUserEMConf.setRequiredConfirmEMailAddr( editBuff.getRequiredConfirmEMailAddr() );
						editSecUserEMConf.setRequiredEMailSentStamp( editBuff.getRequiredEMailSentStamp() );
						editSecUserEMConf.setRequiredEMConfirmationUuid6( editBuff.getRequiredEMConfirmationUuid6() );
						editSecUserEMConf.setRequiredNewAccount( editBuff.getRequiredNewAccount() );
						break;
					case Replace:
						editSecUserEMConf = (ICFBamSecUserEMConfEditObj)origSecUserEMConf.beginEdit();
						editSecUserEMConf.deleteInstance();
						editSecUserEMConf = null;
						origSecUserEMConf = null;
						editSecUserEMConf = editBuff;
						break;
				}
			}

			if( editSecUserEMConf != null ) {
				if( origSecUserEMConf != null ) {
					editSecUserEMConf.update();
				}
				else {
					origSecUserEMConf = (ICFBamSecUserEMConfObj)editSecUserEMConf.create();
				}
				editSecUserEMConf = null;
			}

			curContext.putNamedValue( "Object", origSecUserEMConf );
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
