package com.gdx.orion.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

/**
 * A "paged" scroll pane that auto locks onto discrete pages of screen.<br/>
 * <br/>
 * Based off of: http://stackoverflow.com/a/20735450/5697702
 * 
 * @author jau
 * @see #addPage(Actor)
 * @see #addPage(Actor, float)
 */
public class PagedScrollPane extends ScrollPane {
	/**
	 * Default page gap prevents pages from overlapping and is small to prevent
	 * pages from sometimes getting stuck.
	 */
    private static final float DEFAULT_PAGE_SPACE = 3.0f;

	private boolean wasPanDragFling = false;
    
	/**
	 * This is the internal layout table to hold the pages
	 */
    private final Table table = new Table();
    private float pageSpace;
    private ScrollRunnable onScroll;
    
    public static interface ScrollRunnable {
        public void run(Actor scrolledTo);
    }
    
    /**
     * Creates a paged scroll pane with default between pages to help 
     * prevent page overlap which can sometimes be seen
     */
    public PagedScrollPane () {
        super(null);
        setup(DEFAULT_PAGE_SPACE);
    }
    
    /**
     * Sets the page space "gap" between pages.  Usually want this to be close to zero otherwise
     * paging might get stuck with large gaps.
     * 
     * @param pageSpace  gap between pages
     */
    public PagedScrollPane (float pageSpace) {
        super(null);
        setup(pageSpace);
    }

    public PagedScrollPane (Skin skin, float pageSpace) {
        super(null, skin);
        setup(pageSpace);
    }

    public PagedScrollPane (Skin skin, String styleName, float pageSpace) {
        super(null, skin, styleName);
        setup(pageSpace);
    }

    public PagedScrollPane (Actor widget, ScrollPaneStyle style, float pageSpace) {
        super(null, style);
        setup(pageSpace);
    }

    public void setOnScroll(ScrollRunnable onScroll) {
        this.onScroll = onScroll;
    }

    private void setup(float pageSpace) {
        this.pageSpace = pageSpace;
        this.onScroll = null;

        table.defaults().space(pageSpace);
        super.setWidget(table);
    }

    /**
     * Adds a page that will also be locked onto when scrolling completes
     * 
     * @param page  the page
     * @param minWidth a minimum width for the page
     * @return the table cell for the page
     * @see #addPage(Actor)
     */
    public Cell addPage(final Actor page, final float minWidth) {
    	final Cell<Actor> cell = table.add(page).expandY().fillY();
        
    	if (minWidth >= 0) {
        	return cell.minWidth(minWidth);
    	} else {
    		return cell;
    	}
    }
    
    /**
     * Adds a page that will also be locked onto when scrolling completes
     * 
     * @param  page  the page
     * @return the table cell for the page
     * @see #addPage(Actor, float)
     */
    public Cell addPage(final Actor page) {
    	return addPage(page, -1);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (wasPanDragFling && !isPanning() && !isDragging() && !isFlinging()) {
            wasPanDragFling = false;
            scrollToPage();
        } else {
            if (isPanning() || isDragging() || isFlinging()) {
                wasPanDragFling = true;
            }
        }
    }

    @Override
    public void setWidget (Actor widget) {
        //
    }

    public void setPageSpacing(float pageSpacing) {
        if (table != null) {
            table.defaults().space(pageSpacing);
            for (@SuppressWarnings("rawtypes") Cell cell : table.getCells()) {
                cell.space(pageSpacing);
            }
            table.invalidate();
        }
    }

    private void scrollToPage() {
        final float width = getWidth();
        final float scrollX = getScrollX() + getWidth() / 2f;

        Array<Actor> pages = table.getChildren();
        if (pages.size > 0) {
            for (Actor a : pages) {
                float pageX = a.getX();
                float pageWidth = a.getWidth();

                if (scrollX >= pageX && scrollX < pageX + pageWidth)
                {
                    setScrollX(pageX - (width - pageWidth) / 2f);
                    if (onScroll != null)
                    {
                        onScroll.run(a);
                    }
                    break;
                }
            }
        }
    }

    public void scrollTo(Actor listenerActor) {
        float pageX = listenerActor.getX();
        float pageWidth = listenerActor.getWidth();
        final float width = getWidth();

        setScrollX(pageX - (width - pageWidth) / 2f);
    }

    public void addEmptyPage(float offset) {
        table.add().minWidth(offset);
    }
}