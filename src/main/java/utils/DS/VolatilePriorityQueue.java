package utils.DS;

import java.util.*;

public class VolatilePriorityQueue <T> extends AbstractQueue<T>
{
    private final Comparator<? super T> comparator;

    private final List<T> elements = new ArrayList<T>();

    public VolatilePriorityQueue (Comparator <? super T> comparator)
    {
        this.comparator = comparator;
    }

    @Override
    public boolean offer (T e)
    {
        return elements.add (e);
    }

    @Override
    public T poll ()
    {
        if (elements.isEmpty ()) return null;
        else return elements.remove (getMinimumIndex ());
    }

    @Override
    public T peek ()
    {
        if (elements.isEmpty ()) return null;
        else return elements.get (getMinimumIndex ());
    }

    @Override
    public Iterator<T> iterator ()
    {
        return elements.iterator ();
    }

    @Override
    public int size ()
    {
        return elements.size ();
    }

    private int getMinimumIndex ()
    {
        T e = elements.get (0);
        int index = 0;

        for (int count = elements.size (), i = 1; i < count; i++)
        {
            T ee = elements.get (i);
            if (comparator.compare (e, ee) > 0)
            {
                e = ee;
                index = i;
            }
        }

        return index;
    }
}