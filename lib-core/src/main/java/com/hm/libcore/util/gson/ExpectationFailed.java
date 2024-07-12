package com.hm.libcore.util.gson;


// ************************************************************************

@SuppressWarnings("serial")
public class ExpectationFailed extends Exception
{
	
	// ************************************************************************
	// fields
	
	private String			m_Error;
	
	
	
	// ************************************************************************
	// constructors
	
	// ------------------------------------------------------------------------
	public ExpectationFailed()
	{
		this( "-unknown-" );
	}

	// ------------------------------------------------------------------------
	public ExpectationFailed( String text )
	{
		this( text, null );
	}
	
	// ------------------------------------------------------------------------
	public ExpectationFailed( String text, Throwable cause )
	{
		super( text, cause );
		
		this.m_Error = text;
	}
	
	
	// ************************************************************************
	// Object overrides
	
	// ------------------------------------------------------------------------
	@Override
	public String toString()
	{
		return this.m_Error;
	}
	
}
