package br.edu.ufape.poo.backend.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class OffsetPageRequest extends PageRequest
{
	private static final long serialVersionUID = 1L;
	private int offset;
	private int limit;
	
    public OffsetPageRequest(int offset, int limit)
    {
        super(offset, limit, Sort.unsorted());
        this.offset = offset;
        this.limit = limit;
    }
    
    @Override
    public long getOffset()
    {
        return this.offset;
    }
    
    @Override
    public int getPageSize()
    {
    	return this.limit;
    }
    
    @Override
    public Sort getSort()
    {
    	return Sort.unsorted();
    }

    @Override
    public int getPageNumber()
    {
    	return 0;
    }

    @Override
    public PageRequest next()
    {
    	return null;
    }
    
    @Override
    public PageRequest previous()
    {
    	return null;
    }

    @Override
    public PageRequest previousOrFirst()
    {
    	return this;
    }

    @Override
    public PageRequest first()
    {
    	return this;
    }

    @Override
    public PageRequest withPage(int pageNumber)
    {
    	return null;
    }

    @Override
    public boolean hasPrevious()
    {
    	return false;
    }
}