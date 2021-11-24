/*
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package client.presentation.modules.calendar;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

/**
 *
 * @author Oliver
 */
public class CustomFontAwesomeIconView extends FontAwesomeIconView {

    public CustomFontAwesomeIconView() {
        super(FontAwesomeIcon.ALIGN_JUSTIFY);
    }

    @Override
    public FontAwesomeIcon getDefaultGlyph() {
        return FontAwesomeIcon.ALIGN_JUSTIFY;
    }
}
