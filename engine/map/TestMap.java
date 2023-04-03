package engine.map;

import java.util.List;

public class TestMap {
    private Block[][] blocks;
    private int width;
    private int height;
    private int altrelief = 0;

    public TestMap(int width, int height) {
        this.width = width;
        this.height = height;
        blocks = new Block[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((i < 280 && i > 240) && (j < 200 && j > 105)) {
                    altrelief = 300;
                } else if ((i < 330 && i > 225) && (j < 265 && j > 80)) {
                    altrelief = 200;
                } else if ((i < 210 && i > 105) && (j < 135 && j > 40)) {
                    altrelief = 200;
                } else if ((i < 55 && i > 15) && (j < 55 && j > 15)) {
                    altrelief = 100;
                } else if ((i < 160 && i > 25) && (j < 345 && j > 215)) {
                    altrelief = 100;
                } else {
                    altrelief = 0;
                }

                blocks[i][j] = new Block(i, j, altrelief);
            }
        }
    }

    public Block getBlock(int x, int y) {
        return blocks[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Block getBlock(Block block) {
        return blocks[block.getX()][block.getY()];
    }

    public boolean isOnMap(Block block) {
        return block.getX() >= 0 && block.getX() < width
                && block.getY() >= 0 && block.getY() < height;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public int getAltitude(Block newPosition) {
        return blocks[newPosition.getX()][newPosition.getY()].getAltrelief();
    }

    public List<Block> getUsualAirportPlace() {
        List<Block> list = new java.util.ArrayList<Block>();
        list.add(new Block(250, 250));
        list.add(new Block(0, 250));
        list.add(new Block(250, 0));
        list.add(new Block(10, 8));
        list.add(new Block(150, 150));

        return list;
    }

}
