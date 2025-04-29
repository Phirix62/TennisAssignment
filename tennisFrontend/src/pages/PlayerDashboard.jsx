import { useEffect, useState } from "react";
import api from "../api";
import EditAccountForm from "../components/EditAccountForm";

export default function PlayerDashboard({ username }) {
  const [matches, setMatches] = useState([]);
  const [tournaments, setTournaments] = useState([]);
  const [selectedTournament, setSelectedTournament] = useState("");
  const [message, setMessage] = useState("");
  const [editing, setEditing] = useState(false);

  // Fetch all tournaments
  const fetchTournaments = async () => {
    const res = await api.get("/tournaments");
    setTournaments(res.data);
  };

  // Fetch all matches for the player
  const fetchMatches = async () => {
    const res = await api.get(`/player/${username}/matches`);
    setMatches(res.data);
  };

  // Handle tournament join
  const handleJoin = async () => {
    try {
      const res = await api.post(
        `/player/${username}/join/${selectedTournament}`
      );
      setMessage(res.data);
      fetchMatches(); // Refresh matches after joining
    } catch (err) {
      setMessage("Failed to join tournament.");
    }
  };

  useEffect(() => {
    fetchTournaments();
    fetchMatches();
  }, []);

  // Group matches by tournament
  const groupedMatches = matches.reduce((acc, match) => {
    const tournamentName = match.tournament?.name || "Unknown Tournament";
    if (!acc[tournamentName]) {
      acc[tournamentName] = [];
    }
    acc[tournamentName].push(match);
    return acc;
  }, {});

  return (
    <div>
      <h2>Player Dashboard</h2>
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

      <h3>Join a Tournament</h3>
      <select
        onChange={(e) => setSelectedTournament(e.target.value)}
        value={selectedTournament}
      >
        <option value="">Select Tournament</option>
        {tournaments.map((t) => (
          <option key={t.id} value={t.id}>
            {t.name} - {t.location}
          </option>
        ))}
      </select>
      <button onClick={handleJoin} disabled={!selectedTournament}>
        Join
      </button>
      <p>{message}</p>

      <h3>Your Matches</h3>
      {Object.keys(groupedMatches).length === 0 && <p>No matches yet.</p>}
      {Object.entries(groupedMatches).map(([tournamentName, matches]) => (
        <div key={tournamentName}>
          <h4>{tournamentName}</h4>
          <ul>
            {matches.map((m) => (
              <li key={m.id}>
                {m.player1?.username} vs {m.player2?.username} <br />
                {new Date(m.matchTime).toLocaleString()} <br />
                Referee: {m.referee?.username}
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
}
