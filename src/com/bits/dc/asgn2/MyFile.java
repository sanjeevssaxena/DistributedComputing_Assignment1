package com.bits.dc.asgn2;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class MyFile extends File {
    private static final long serialVersionUID = 1L;

    public MyFile(final String pathname) {
        super(pathname);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final MyFile other = (MyFile) obj;
        if (!Arrays.equals(this.getContent(), other.getContent())) {
            return false;
        }
        if (this.getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!this.getName().equals(other.getName())) {
            return false;
        }
        if (this.length() != other.length()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime;
        result = (prime * result) + Arrays.hashCode(this.getContent());
        result = (prime * result) + ((this.getName() == null) ? 0 : this.getName().hashCode());
        result = (prime * result) + (int) (this.length() ^ (this.length() >>> 32));
        return result;
    }

    private byte[] getContent() {
        try (final FileInputStream fis = new FileInputStream(this)) {
            return fis.readAllBytes();
        } catch (final IOException e) {
            e.printStackTrace();
            return new byte[] {};
        }
    }
}