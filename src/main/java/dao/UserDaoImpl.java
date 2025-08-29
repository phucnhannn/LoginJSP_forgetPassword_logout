package dao;

import model.User;
import utils.DBConnection;

import java.sql.*;

public class UserDaoImpl implements UserDao {
    public Connection conn = null;
    public PreparedStatement ps = null;
    public ResultSet rs = null;

    @Override
    public User get(String username) {
        String sql = "SELECT * FROM [user] WHERE userName = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while (rs.next()) {
                return mapUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(rs, ps, conn);
        }
        return null;
    }

    @Override
    public User getByUsernameOrEmail(String term) {
        String sql = "SELECT * FROM [user] WHERE userName = ? OR email = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, term);
            ps.setString(2, term);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(rs, ps, conn);
        }
        return null;
    }

    @Override
    public User getByResetToken(String token) {
        String sql = "SELECT * FROM [user] WHERE resetToken = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(rs, ps, conn);
        }
        return null;
    }

    @Override
    public boolean saveResetToken(int userId, String token, Timestamp expiry) {
        String sql = "UPDATE [user] SET resetToken = ?, resetExpiry = ? WHERE id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, token);
            ps.setTimestamp(2, expiry);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(null, ps, conn);
        }
        return false;
    }

    @Override
    public boolean clearResetToken(int userId) {
        String sql = "UPDATE [user] SET resetToken = NULL, resetExpiry = NULL WHERE id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(null, ps, conn);
        }
        return false;
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE [user] SET passWord = ? WHERE id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(null, ps, conn);
        }
        return false;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setUserName(rs.getString("userName"));
        user.setFullName(rs.getString("fullName"));
        user.setPassWord(rs.getString("passWord"));
        user.setAvatar(rs.getString("avatar"));
        user.setRoleid(rs.getInt("roleid"));
        user.setPhone(rs.getString("phone"));
        user.setCreatedDate(rs.getDate("createdDate"));
        // new fields
        try {
            user.setResetToken(rs.getString("resetToken"));
        } catch (SQLException ignored) {}
        try {
            user.setResetExpiry(rs.getTimestamp("resetExpiry"));
        } catch (SQLException ignored) {}
        return user;
    }

    private void closeQuietly(ResultSet rs, Statement st, Connection c) {
        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
        try { if (st != null) st.close(); } catch (Exception ignored) {}
        try { if (c != null) c.close(); } catch (Exception ignored) {}
    }
}