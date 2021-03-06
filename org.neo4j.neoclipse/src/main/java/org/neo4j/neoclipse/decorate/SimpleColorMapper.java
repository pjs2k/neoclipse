/**
 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.neo4j.neoclipse.decorate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Map some type to colors.
 * 
 * @author Anders Nawroth
 * @param <T> type to map
 */
public class SimpleColorMapper<T>
{
    private static class Colors
    {
        private final Color[] colors;
        private final int size;

        public Colors( final float hue, final float[] saturations,
                final float[] brightnesses )
        {
            size = saturations.length;
            colors = new Color[size];
            for ( int i = 0; i < size; i++ )
            {
                colors[i] = new Color( Display.getDefault(), new RGB( hue,
                        saturations[i], brightnesses[i] ) );
            }
        }

        public Color getColor( final int index )
        {
            return colors[index];
        }

    }

    /**
     * Map Objects to Colors for the graph.
     */
    private final Map<T, Colors> colorMap = new HashMap<T, Colors>();
    /**
     * Create colors.
     */
    private final SimpleHueGenerator hueGenerator = new SimpleHueGenerator();

    private final float[] saturations;
    private final float[] brightnesses;

    public SimpleColorMapper( final float[] saturations,
            final float[] brightnesses )
    {
        if ( saturations == null )
        {
            throw new IllegalArgumentException( "Null saturations array given." );
        }
        if ( brightnesses == null )
        {
            throw new IllegalArgumentException(
                    "Null brightnesses array given." );
        }
        if ( saturations.length == 0 )
        {
            throw new IllegalArgumentException(
                    "Zero length saturations array given." );
        }
        if ( brightnesses.length == 0 )
        {
            throw new IllegalArgumentException(
                    "Zero length brightnesses array given." );
        }
        if ( saturations.length != brightnesses.length )
        {
            throw new IllegalArgumentException( "Different size arrays given." );
        }
        this.saturations = saturations.clone();
        this.brightnesses = brightnesses.clone();
    }

    public Color getColor( final T type, final int index )
    {
        Colors colors = colorMap.get( type );
        if ( colors == null )
        {
            colors = new Colors( hueGenerator.nextHue(), saturations,
                    brightnesses );
            colorMap.put( type, colors );
        }
        return colors.getColor( index );
    }

    public boolean colorExists( final T type )
    {
        return colorMap.containsKey( type );
    }

    public Set<T> getKeys()
    {
        return colorMap.keySet();
    }
}
