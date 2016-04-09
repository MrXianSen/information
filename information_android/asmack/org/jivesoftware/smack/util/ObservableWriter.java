/**
 * $RCSfile$
 * $Revision$
 * $Date$
 * <p>
 * Copyright 2003-2007 Jive Software.
 * <p>
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.smack.util;

import java.io.*;
import java.util.*;

/**
 * An ObservableWriter is a wrapper on a Writer that notifies to its listeners when
 * writing to character streams.
 *
 * @author Gaston Dombiak
 */
public class ObservableWriter extends Writer {

    Writer wrappedWriter = null;
    List<WriterListener> listeners = new ArrayList<WriterListener>();

    public ObservableWriter(Writer wrappedWriter) {
        this.wrappedWriter = wrappedWriter;
    }

    @Override
    public void write(char cbuf[], int off, int len) throws IOException {
        wrappedWriter.write(cbuf, off, len);
        String str = new String(cbuf, off, len);
        notifyListeners(str);
    }

    @Override
    public void flush() throws IOException {
        wrappedWriter.flush();
    }

    @Override
    public void close() throws IOException {
        wrappedWriter.close();
    }

    @Override
    public void write(int c) throws IOException {
        wrappedWriter.write(c);
    }

    @Override
    public void write(char cbuf[]) throws IOException {
        wrappedWriter.write(cbuf);
        String str = new String(cbuf);
        notifyListeners(str);
    }

    @Override
    public void write(String str) throws IOException {
        wrappedWriter.write(str);
        notifyListeners(str);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        wrappedWriter.write(str, off, len);
        str = str.substring(off, off + len);
        notifyListeners(str);
    }

    /**
     * Notify that a new string has been written.
     *
     * @param str the written String to notify 
     */
    private void notifyListeners(String str) {
        WriterListener[] writerListeners = null;
        synchronized (listeners) {
            writerListeners = new WriterListener[listeners.size()];
            listeners.toArray(writerListeners);
        }
        for (int i = 0; i < writerListeners.length; i++) {
            writerListeners[i].write(str);
        }
    }

    /**
     * Adds a writer listener to this writer that will be notified when
     * new strings are sent.
     *
     * @param writerListener a writer listener.
     */
    public void addWriterListener(WriterListener writerListener) {
        if (writerListener == null) {
            return;
        }
        synchronized (listeners) {
            if (!listeners.contains(writerListener)) {
                listeners.add(writerListener);
            }
        }
    }

    /**
     * Removes a writer listener from this writer.
     *
     * @param writerListener a writer listener.
     */
    public void removeWriterListener(WriterListener writerListener) {
        synchronized (listeners) {
            listeners.remove(writerListener);
        }
    }

}
