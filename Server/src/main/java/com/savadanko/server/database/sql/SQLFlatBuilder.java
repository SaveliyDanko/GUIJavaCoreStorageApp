package com.savadanko.server.database.sql;

import com.savadanko.common.models.*;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;

@Setter
public class SQLFlatBuilder implements ModelBuilder<Flat>{
    private static final Logger logger = LogManager.getLogger(SQLDataBase.class);

    private Connection connection;

    public SQLFlatBuilder(){
    }

    public void createTables(){
        String deleteOnCascadeHouseFunc = "CREATE OR REPLACE FUNCTION delete_from_houses() RETURNS TRIGGER AS $$\n" +
                "BEGIN\n" +
                "    DELETE FROM houses WHERE id = OLD.house_id;\n" +
                "    RETURN OLD;\n" +
                "END;\n" +
                "$$ LANGUAGE plpgsql;";

        String deleteOnCascadeCordsFunc = "CREATE OR REPLACE FUNCTION delete_from_cords() RETURNS TRIGGER AS $$\n" +
                "BEGIN\n" +
                "    DELETE FROM coordinates WHERE id = OLD.coordinates_id;\n" +
                "    RETURN OLD;\n" +
                "END;\n" +
                "$$ LANGUAGE plpgsql;\n";

        String trigger1 = "CREATE OR REPLACE TRIGGER after_delete_house\n" +
                "AFTER DELETE ON flats\n" +
                "FOR EACH ROW\n" +
                "EXECUTE FUNCTION delete_from_houses();\n";

        String trigger2 = "CREATE OR REPLACE TRIGGER after_delete_coordinates\n" +
                "AFTER DELETE ON flats\n" +
                "FOR EACH ROW\n" +
                "EXECUTE FUNCTION delete_from_cords();\n";

        String coordinatesTable = "CREATE TABLE IF NOT EXISTS coordinates (" +
                "id SERIAL PRIMARY KEY," +
                "x FLOAT," +
                "y BIGINT);";

        String houseTable = "CREATE TABLE IF NOT EXISTS houses (" +
                "id SERIAL PRIMARY KEY," +
                "house_name VARCHAR(64)," +
                "year BIGINT," +
                "numberOfFloors BIGINT," +
                "numberOfFlatsOnFloor INT," +
                "numberOfLifts INT);";

        String flatTable = "CREATE TABLE IF NOT EXISTS flats (" +
                "id SERIAL PRIMARY KEY," +
                "flat_name VARCHAR(64)," +
                "coordinates_id BIGINT," +
                "house_id BIGINT," +
                "date TIMESTAMPTZ," +
                "area FLOAT," +
                "numberOfRooms BIGINT," +
                "price FLOAT," +
                "view VARCHAR(16)," +
                "transport VARCHAR(16)," +
                "owner VARCHAR(128)," +
                "FOREIGN KEY (coordinates_id) REFERENCES coordinates(id) ON DELETE CASCADE," +
                "FOREIGN KEY (house_id) REFERENCES houses(id) ON DELETE CASCADE," +
                "FOREIGN KEY (owner) REFERENCES users(login));";

        try (Statement statement = connection.createStatement()){
            statement.execute(deleteOnCascadeHouseFunc);
            statement.execute(deleteOnCascadeCordsFunc);
            statement.execute(coordinatesTable);
            statement.execute(houseTable);
            statement.execute(flatTable);
            statement.execute(trigger1);
            statement.execute(trigger2);
        }
        catch (SQLException e){
            logger.error(e.getMessage(), e);
        }
    }

    public long createModel(Flat flat){
        long cordsId = saveCoordinates(flat.getCoordinates());
        long houseId = saveHouse(flat.getHouse());
        return saveFlat(flat, cordsId, houseId);
    }

    public Flat readModel(long id){
        String query = "select * from flats " +
                "join coordinates on flats.coordinates_id = coordinates.id " +
                "join houses on flats.house_id = houses.id " +
                "where flats.id = ?;";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()){
                resultSet.next();
                long flatId = resultSet.getLong("id");
                String name = resultSet.getString("flat_name");
                float x = resultSet.getFloat("x");
                long y = resultSet.getLong("y");

                Timestamp timestamp = resultSet.getTimestamp("date");
                ZonedDateTime dateTime = timestamp.toInstant().atZone(ZoneId.systemDefault());

                float area = resultSet.getFloat("area");
                long numberOfRooms = resultSet.getLong("numberOfRooms");
                float price = resultSet.getFloat("price");
                View view = View.valueOf(resultSet.getString("view"));
                Transport transport = Transport.valueOf(resultSet.getString("transport"));
                String houseName = resultSet.getString("house_name");
                long year = resultSet.getLong("year");
                long numberOfFloors = resultSet.getLong("numberOfFloors");
                int numberOfFlatsOnFloor = resultSet.getInt("numberOfFlatsOnFloor");
                int numberOfLifts = resultSet.getInt("numberOfLifts");
                String owner = resultSet.getString("owner");

                return new Flat(
                        flatId,
                        name,
                        new Coordinates(x, y),
                        dateTime,
                        area,
                        numberOfRooms,
                        price,
                        view,
                        transport,
                        new House(
                                houseName,
                                year,
                                numberOfFloors,
                                numberOfFlatsOnFloor,
                                numberOfLifts
                        ),
                        owner
                );
            }
        }
        catch (SQLException e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public LinkedHashMap<Long, Flat> readAll() {
        String query = "SELECT * FROM flats " +
                "JOIN houses ON house_id = houses.id " +
                "JOIN coordinates ON coordinates_id = coordinates.id;";

        LinkedHashMap<Long, Flat> linkedHashMap = new LinkedHashMap<>();

        try(PreparedStatement statement = connection.prepareStatement(query)){
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    long flatId = resultSet.getLong("id");
                    String name = resultSet.getString("flat_name");
                    float x = resultSet.getFloat("x");
                    long y = resultSet.getLong("y");

                    Timestamp timestamp = resultSet.getTimestamp("date");
                    ZonedDateTime dateTime = timestamp.toInstant().atZone(ZoneId.systemDefault());

                    float area = resultSet.getFloat("area");
                    long numberOfRooms = resultSet.getLong("numberOfRooms");
                    float price = resultSet.getFloat("price");
                    View view = View.valueOf(resultSet.getString("view"));
                    Transport transport = Transport.valueOf(resultSet.getString("transport"));
                    String houseName = resultSet.getString("house_name");
                    long year = resultSet.getLong("year");
                    long numberOfFloors = resultSet.getLong("numberOfFloors");
                    int numberOfFlatsOnFloor = resultSet.getInt("numberOfFlatsOnFloor");
                    int numberOfLifts = resultSet.getInt("numberOfLifts");
                    String owner = resultSet.getString("owner");

                    Flat flat = new Flat(
                            flatId,
                            name,
                            new Coordinates(x, y),
                            dateTime,
                            area,
                            numberOfRooms,
                            price,
                            view,
                            transport,
                            new House(
                                    houseName,
                                    year,
                                    numberOfFloors,
                                    numberOfFlatsOnFloor,
                                    numberOfLifts
                            ),
                            owner
                    );

                    linkedHashMap.put(flatId, flat);
                }
            }
        }
        catch (SQLException e){
            logger.error(e.getMessage());
        }
        return linkedHashMap;
    }

    @Override
    public void updateModel(long id, Flat flat) {
        updateHouse(id, flat);
        updateCoordinates(id, flat);
        updateFlat(id, flat);
    }

    @Override
    public void deleteModel(long id) {
        String query = "DELETE FROM flats WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, id);

            statement.executeUpdate();
        }
        catch (SQLException e){
            logger.error(e.getMessage());
        }
    }

    private void updateFlat(long id, Flat flat){
        String query = "UPDATE flats SET flat_name = ?, " +
                "coordinates_id = ?, " +
                "house_id = ?, " +
                "date = ?, " +
                "area = ?, " +
                "numberOfRooms = ?, " +
                "price = ?, " +
                "view = ?, " +
                "transport = ?, " +
                "owner = ? " +
                "WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, flat.getName());
            statement.setLong(2, id);
            statement.setLong(3, id);
            OffsetDateTime creationDate = flat.getCreationDate().toOffsetDateTime();
            statement.setObject(4, creationDate, Types.TIMESTAMP_WITH_TIMEZONE);
            statement.setFloat(5, flat.getArea());
            statement.setLong(6, flat.getNumberOfRooms());
            statement.setFloat(7, flat.getPrice());
            statement.setString(8, flat.getView().name());
            statement.setString(9, flat.getTransport().name());
            statement.setString(10, flat.getOwner());
            statement.setLong(11, id);

            statement.executeUpdate();
        }
        catch (SQLException e){
            logger.error(e.getMessage());
        }
    }

    private void updateHouse(long id, Flat flat){
        String query = "UPDATE houses SET house_name = ?," +
                "year = ?," +
                "numberOfFloors = ?," +
                "numberOfFlatsOnFloor = ?," +
                "numberOfLifts = ? " +
                "WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)){
            House house = flat.getHouse();
            statement.setString(1, house.getName());
            statement.setLong(2, house.getYear());
            statement.setLong(3, house.getNumberOfFloors());
            statement.setLong(4, house.getNumberOfFlatsOnFloor());
            statement.setLong(5, house.getNumberOfLifts());
            statement.setLong(6, id);

            statement.executeUpdate();
        }
        catch (SQLException e){
            logger.error(e.getMessage(), e);
        }
    }

    private void updateCoordinates(long id, Flat flat){
        String query = "UPDATE coordinates SET x = ?," +
                "y = ? " +
                "WHERE id = ?;";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            Coordinates coordinates = flat.getCoordinates();

            statement.setFloat(1, coordinates.getX());
            statement.setLong(2, coordinates.getY());
            statement.setLong(3, id);

            statement.executeUpdate();
        }
        catch (SQLException e){
            logger.error(e.getMessage(), e);
        }
    }

    private long saveFlat(Flat flat, long houseId, long cordsId){
        String query = "INSERT INTO flats (flat_name, coordinates_id, house_id, date, area, numberOfRooms, price, view, transport, owner)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, flat.getName());
            statement.setLong(2, cordsId);
            statement.setLong(3, houseId);
            OffsetDateTime creationDate = flat.getCreationDate().toOffsetDateTime();
            statement.setObject(4, creationDate, Types.TIMESTAMP_WITH_TIMEZONE);
            statement.setFloat(5, flat.getArea());
            statement.setLong(6, flat.getNumberOfRooms());
            statement.setFloat(7, flat.getPrice());
            statement.setString(8, flat.getView().name());
            statement.setString(9, flat.getTransport().name());
            statement.setString(10, flat.getOwner());

            statement.executeUpdate();

            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    long flatId = generatedKeys.getLong(1);
                    flat.setId(flatId);
                    return flatId;
                }
            }
        }
        catch (SQLException e){
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    private long saveHouse(House house){
        String query = "INSERT INTO houses (house_name, year, numberOfFloors, numberOfFlatsOnFloor, numberOfLifts) VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, house.getName());
            statement.setLong(2, house.getYear());
            statement.setLong(3, house.getNumberOfFloors());
            statement.setInt(4, house.getNumberOfFlatsOnFloor());
            statement.setInt(5, house.getNumberOfLifts());

            statement.executeUpdate();

            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    long houseId = generatedKeys.getLong(1);
                    house.setId(houseId);
                    return houseId;
                }
            }
        }
        catch (SQLException e){
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    private long saveCoordinates(Coordinates coordinates){
        String query = "INSERT INTO coordinates (x, y) VALUES (?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setFloat(1, coordinates.getX());
            statement.setLong(2, coordinates.getY());

            statement.executeUpdate();

            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    long cordsId = generatedKeys.getLong(1);
                    coordinates.setId(cordsId);
                    return cordsId;
                }
            }
        }
        catch (SQLException e){
            logger.error(e.getMessage(), e);
        }
        return 0;
    }
}
