import { useEffect, useState } from "react";
import api from "../api";
import EditAccountForm from "../components/EditAccountForm";

export default function RefereeDashboard({ username }) {
  const [matches, setMatches] = useState([]);
  const [players, setPlayers] = useState([]);
  const [nameFilter, setNameFilter] = useState("");
  const [minMatches, setMinMatches] = useState("");
  const [message, setMessage] = useState("");
  const [editing, setEditing] = useState(false);
  const [tournaments, setTournaments] = useState([]);
  const [selectedTournament, setSelectedTournament] = useState("");

  useEffect(() => {
    const fetchMatches = async () => {
      const res = await api.get(`/referee/${username}/matches`);
      setMatches(res.data);
    };
    const fetchTournaments = async () => {
      const res = await api.get("/tournaments"); // or adjust route as needed
      setTournaments(res.data);
    };

    fetchMatches();
    fetchTournaments();
    fetchPlayers();
  }, [username]);

  const fetchPlayers = async () => {
    try {
      const params = {};
      if (nameFilter) params.name = nameFilter;
      if (minMatches) params.minMatches = minMatches;
      if (selectedTournament) params.tournamentId = selectedTournament;

      const res = await api.get("/referee/players", { params });
      setPlayers(res.data);
      setMessage(`Found ${res.data.length} players`);
    } catch {
      setMessage("Error fetching players.");
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchPlayers();
  };

  const groupedMatches = matches.reduce((acc, match) => {
    const tournament = match.tournament?.name || "Unknown Tournament";
    if (!acc[tournament]) {
      acc[tournament] = [];
    }
    acc[tournament].push(match);
    return acc;
  }, {});

  return (
    <div>
      <h2>Referee Dashboard</h2>

      <button onClick={() => setEditing(!editing)}>
        {editing ? "Close Edit" : "Edit Account"}
      </button>

      {editing && (
        <EditAccountForm
          currentUsername={username}
          onUpdate={(newUsername) => {
            setEditing(false);
            // Optional: refresh or notify App that username changed
          }}
        />
      )}

      <h3>Matchs to supervise</h3>
      {Object.entries(groupedMatches).map(([tournament, matches]) => (
        <div key={tournament}>
          <h4>{tournament}</h4>
          <ul>
            {matches.map((m) => (
              <li key={m.id}>
                {m.player1?.username} vs {m.player2?.username} at {m.matchTime}
              </li>
            ))}
          </ul>
        </div>
      ))}

      <h3>Filter Players</h3>
      <form onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Player name"
          value={nameFilter}
          onChange={(e) => setNameFilter(e.target.value)}
        />
        <input
          type="number"
          placeholder="Min matches"
          value={minMatches}
          onChange={(e) => setMinMatches(e.target.value)}
        />
        <select
          value={selectedTournament}
          onChange={(e) => setSelectedTournament(e.target.value)}
        >
          <option value="">All Tournaments</option>
          {tournaments.map((t) => (
            <option key={t.id} value={t.id}>
              {t.name}
            </option>
          ))}
        </select>
        <button type="submit">Search</button>
      </form>

      {message && <p>{message}</p>}

      {players.length > 0 && (
        <table border="1" cellPadding="6">
          <thead>
            <tr>
              <th>Username</th>
              <th>Full Name</th>
              <th>Email</th>
            </tr>
          </thead>
          <tbody>
            {players.map((p) => (
              <tr key={p.id}>
                <td>{p.username}</td>
                <td>{p.fullName}</td>
                <td>{p.email}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {players.length === 0 && <p>No players match your criteria.</p>}
    </div>
  );
}
