package org.josmer.utils;

import java.util.LinkedList;

public class GridReader {
    private final String rowDelimiter;
    private final String columnDelimiter;
    private final int firstRow;
    private LinkedList<LinkedList<String>> grid;

    public GridReader(final String rowDelimiter, final String columnDelimiter, int firstRow) {
        this.rowDelimiter = rowDelimiter;
        this.columnDelimiter = columnDelimiter;
        this.firstRow = firstRow;
        this.grid = new LinkedList<>();
    }

    public LinkedList<LinkedList<String>> get(final String path) {
        String file = Toolbox.readFile(path);

        final String[] rows = file.split(rowDelimiter);

        for (int row = firstRow; row < rows.length; row++) {
            final String[] columns = rows[row].split(columnDelimiter);
            LinkedList<String> columnsGrid = new LinkedList<>();
            for (int column = 0; column < columns.length; column++) {
                columnsGrid.add(columns[column]);
            }
            grid.add(columnsGrid);
        }

        return grid;
    }
}
