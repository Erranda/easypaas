package com.withinet.opaas.controller.system.impl;

/*
 * Copyright 2008 Alin Dreghiciu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ops4j.io.Pipe;
import org.ops4j.pax.runner.commons.Info;
import org.ops4j.pax.runner.platform.PlatformException;
import org.ops4j.pax.runner.platform.StoppableJavaRunner;
import org.ops4j.pax.runner.platform.internal.CommandLineBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Pax Java Runner modified by Folarin Omotoriogun to support multithreading and process object return
 * 
 * @author Alin Dreghiciu (adreghiciu@gmail.com)
 * @since 0.6.1, December 09, 2008
 */
public class PaxJavaRunner
        implements StoppableJavaRunner
{

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog( PaxJavaRunner.class );

    /**
     * If the execution should wait for platform shutdown.
     */
    private final boolean m_wait;
    /**
     * Framework process.
     */
    private Process m_frameworkProcess;
    /**
     * Command Hash Map in case of Shell Script;
     */
    HashMap<String, String[]> commandMap;
    /**
     * Constructor.
     */
    public PaxJavaRunner()
    {
        this( true );
    }

    /**
     * Constructor.
     *
     * @param wait should wait for framework exis
     */
    public PaxJavaRunner(boolean wait)
    {
        m_wait = wait;
    }

    public void exec( final String[] vmOptions,
                                   final String[] classpath,
                                   final String mainClass,
                                   final String[] programOptions,
                                   final String javaHome,
                                   final File workingDirectory )
            throws PlatformException
    {
        exec( vmOptions,classpath,mainClass,programOptions,javaHome,workingDirectory,new String [0] );
    }
    /**
     * {@inheritDoc}
     * @return
     */
    public void exec( final String[] vmOptions,
                                   final String[] classpath,
                                   final String mainClass,
                                   final String[] programOptions,
                                   final String javaHome,
                                   final File workingDirectory,
                                   final String[] envOptions )
            throws PlatformException
    {
        if( m_frameworkProcess != null )
        {
            throw new PlatformException( "Platform already started" );
        }

        final StringBuilder cp = new StringBuilder();

        for( String path : classpath )
        {
            if( cp.length() != 0 )
            {
                cp.append( File.pathSeparator );
            }
            cp.append( path );
        }

        final CommandLineBuilder commandLine = new CommandLineBuilder()
                .append( getJavaExecutable( javaHome ) )
                .append( vmOptions )
                .append( "-cp" )
                .append( cp.toString() )
                .append( mainClass )
                .append( programOptions );

        LOG.debug( "Start command line [" + Arrays.toString( commandLine.toArray() ) + "]" );

        try
        {
            LOG.debug( "Starting platform process." );
            m_frameworkProcess = Runtime.getRuntime().exec( commandLine.toArray(), createEnvironmentVars( envOptions ), workingDirectory );
        }
        catch( IOException e )
        {
            throw new PlatformException( "Could not start up the process", e );
        }

        LOG.info( "Runner has successfully finished his job!" );
        if (m_wait == true ) {
            try {
                this.m_frameworkProcess.waitFor();
            } catch (InterruptedException e) {
                LOG.debug(e.toString());
            }
        }
    }
    /**
     * Added method to obtain framework process launched
     * @return
     */
    public Process getFrameWorkProcess () {
        return this.m_frameworkProcess;
    }
    private String[] createEnvironmentVars( String[] envOptions )
    {
        List<String> env = new ArrayList<String>(  );
        Map<String, String> getenv = System.getenv();
        for (String key : getenv.keySet() ) {
            env.add( key + "=" + getenv.get( key ) );
        }
        if (envOptions != null) Collections.addAll( env, envOptions );
        return env.toArray( new String[env.size()] );
    }


    /**
     * Return path to java executable.
     *
     * @param javaHome java home directory
     *
     * @return path to java executable
     *
     * @throws PlatformException if java home could not be located
     */
    static String getJavaExecutable( final String javaHome )
            throws PlatformException
    {
        if( javaHome == null )
        {
            throw new PlatformException( "JAVA_HOME is not set." );
        }
        return javaHome + "/bin/java";
    }

	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
