package ch.mse.santachallenge.utils;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Random;

import ch.mse.santachallenge.Gift;
import ch.mse.santachallenge.Location;
import ch.mse.santachallenge.Partition;
import ch.mse.santachallenge.abstraction.ITrip;

/**
 * 
 * @author Marek Arnold (arnd@zhaw.ch)
 *
 *         Use this class to generate visual representations of a solution.
 */
public class Printer {
    private double lineWidth;
    private double giftRadius;

    private final double margin = 100;
    private final double xMult = 100.0;
    private final double yMult = -100.0;

    private final double maxX = 180.0 * Math.abs(xMult);
    private final double maxY = 90.0 * Math.abs(yMult);

    private final double minX = -180.0 * Math.abs(xMult);
    private final double minY = -90.0 * Math.abs(yMult);

    private final double xTransform = margin - minX;
    private final double yTransform = margin - minY;
    private final int height = (int) (maxY + margin + yTransform), width = (int) (maxX + xTransform + margin);

    private final Random rm = new Random();

    public Printer() {
        this(1, 5);
    }

    /**
     * Creates a new Printer.
     * 
     * @param lineWidth
     * @param pointRadius
     */
    public Printer(double lineWidth, double pointRadius) {
        super();
        this.lineWidth = lineWidth;
        this.giftRadius = pointRadius;
    }

    /**
     * Export an instance and it's corresponding solution to a HTML file containing
     * an SVG representing the solution.
     *
     * @param path  file to write to.
     * @param gifts the gifts to draw
     * @param trips the trips to draw
     * @throws IOException Can throw IOExceptions
     */
    public void writeToHtml(String path, Iterable<Gift> gifts, Iterable<ITrip> trips) throws IOException {
        LinkedList<Iterable<Gift>> tripOfGifts = new LinkedList<>();
        for (ITrip iTrip : trips) {
            tripOfGifts.add(iTrip.getDistributedGifts());
        }
        writeToHtml2(path, gifts, tripOfGifts);
    }

    /**
     * Export an instance and it's corresponding solution to a HTML file containing
     * an SVG representing the solution.
     * 
     * @param path        to write to.
     * @param gifts       the gifts to draw
     * @param tripOfGifts trips containing the gifts
     * @throws IOException
     */
    public void writeToHtml2(String path, Iterable<Gift> gifts, Iterable<Iterable<Gift>> tripOfGifts) throws IOException {
        writeToHtml2(path, gifts, tripOfGifts, null);
    }

    /**
     * Export an instance and it's corresponding solution to a HTML file containing
     * an SVG representing the solution.
     *
     * @param path        to write to.
     * @param gifts       the gifts to draw
     * @param tripOfGifts trips containing the gifts
     * @param partitions all partitions
     * @throws IOException
     */
    public void writeToHtml2(String path, Iterable<Gift> gifts, Iterable<Iterable<Gift>> tripOfGifts, Iterable<Partition> partitions)
            throws IOException {
        File file = new File(path).getAbsoluteFile();
        String title = "SantaChallenge";
        String comment = "Comment";

        Files.createDirectories(file.toPath().getParent());
        try (BufferedWriter htmlWriter = Files.newBufferedWriter(file.toPath())) {
            htmlWriter.write("<html>");
            htmlWriter.write("<head>");
            htmlWriter.write("<style>");
            htmlWriter.write(".hoverbg {background:rgba(255,255,255,0.3);}");
            htmlWriter.write(".hoverbg:hover {background:rgba(255,255,255,1.0);}");
            htmlWriter.write("</style>");
            htmlWriter.write("<title>" + title + "</title>");
            htmlWriter.write("</head>");
            htmlWriter.write("<body>");
            htmlWriter.write("<div class=\"hoverbg\" style=\"position:absolute; top:10;left:10; z-index:100;\">");
            htmlWriter.write("<p style=\"font-size:30px\">" + title + "</p>");
            htmlWriter.write("<p style=\"font-size:15px\">" + comment + "</p>");
            // svgWriter.write("<p style=\"font-size:30px\">Total distance: " +
            // ((int)Math.rint(Utils.euclideanDistance2D(solution) * 10) / 10.0) + "</p>");
            htmlWriter.write("</div>");
            writeSvg(gifts, tripOfGifts, partitions, htmlWriter);
            htmlWriter.write("</body>");
            htmlWriter.write("</html>");
        }
    }

    private void writeSvg(Iterable<Gift> gifts, Iterable<Iterable<Gift>> tripOfGifts, Iterable<Partition> partitions, BufferedWriter svgWriter)
            throws IOException {
        svgWriter.write("<svg viewBox=\"0 0 " + width + " " + height
                + "\" style=\"position:absolute; top:0; left:0; bottom:0; right:0; z-index:1;\">");
        for (Gift gift : gifts) {
            writeSVGPoint(gift.getLocation(), giftRadius, svgWriter);
        }

        printLocationRaster(svgWriter);

        if (tripOfGifts != null) {
            Location santasHome = new Location(0, 90);
            Location lastLocation;
            for (Iterable<Gift> trip : tripOfGifts) {
                lastLocation = santasHome;
                for (Gift gift : trip) {
                    writeSvgLine(lastLocation, gift.getLocation(), svgWriter);
                    lastLocation = gift.getLocation();
                }
                writeSvgLine(lastLocation, santasHome, svgWriter);
            }
        }

        if (partitions != null){
            for(var partition : partitions){
                var color = getRandomColor();
                for(var gift : partition){
                    writeSVGPoint(gift.getLocation(), giftRadius * 5, color, svgWriter);
                }
            }
        }

        svgWriter.write("</svg>");
    }

    private void printLocationRaster(BufferedWriter svgWriter) throws IOException {
        for(var longitude = -180; longitude <= 360; longitude += 5){
            var width = lineWidth * 5;
            if((longitude + 180) % 30 == 0){
                width *= 2;
            }
            if(longitude == 0){
                width *= 2;
            }
            this.writeSvgLine(
                    new Location(longitude, -90.0),
                    new Location(longitude, 90.0),
                    svgWriter,
                    width,
                    Color.BLACK);
        }

        for(var latitude = -90; latitude <= 90; latitude += 5){
            var width = lineWidth * 5;
            if((latitude + 90) % 30 == 0){
                width *= 2;
            }
            if(latitude == 0){
                width *= 2;
            }
            this.writeSvgLine(
                    new Location(-180.0, latitude),
                    new Location(180.0, latitude),
                    svgWriter,
                    width,
                    Color.BLACK);
        }
    }

    private void writeSVGPoint(Location location, double pointRadius, BufferedWriter svgWriter) throws IOException {
        writeSVGPoint(location, pointRadius, Color.BLACK, svgWriter);
    }

    private void writeSVGPoint(Location location, double pointRadius, Color color, BufferedWriter svgWriter) throws IOException {
        svgWriter.write("<circle cx=\"" + (int) Math.rint(location.getLongitude() * xMult + xTransform) + "\" cy=\""
                + (int) Math.rint(location.getLatitude() * yMult + yTransform) + "\" r=\"" + pointRadius
                + "\" stroke=\"black\" stroke-width=\"0\" fill=\" "+ getSvgColorString(color) +" \"/>");
    }

    private void writeSvgLine(Location a, Location b, BufferedWriter svgWriter) throws IOException {

        writeSvgLine(a, b, svgWriter, lineWidth,  Color.RED);
    }

    private void writeSvgLine(Location a, Location b, BufferedWriter svgWriter, double lineWidth, Color color) throws IOException {
        svgWriter.write("<line x1=\"" + (int) Math.rint(a.getLongitude() * xMult + xTransform) + "\" y1=\""
                + (int) Math.rint(a.getLatitude() * yMult + yTransform) + "\" x2=\""
                + (int) Math.rint(b.getLongitude() * xMult + xTransform) + "\" y2=\""
                + (int) Math.rint(b.getLatitude() * yMult + yTransform) + "\" style=\"stroke:" + getSvgColorString(color) + ";stroke-width:"
                + (int) lineWidth + "\"/>");
    }

    private String getSvgColorString(Color color) {
        return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
    }

    private Color getRandomColor(){
        float r = rm.nextFloat();
        float g = rm.nextFloat();
        float b = rm.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor.darker();
    }
}
