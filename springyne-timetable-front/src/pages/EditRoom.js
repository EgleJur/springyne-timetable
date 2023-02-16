import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import { TextField } from "@mui/material";

function EditRoomPage() {
  const [room, setRoom] = useState({});
  const [nameError, setNameError] = useState("");
  const [buildingError, setBuildingError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [changed, setChanged] = useState(false);
  const params = useParams();

  const fetchRoom = () => {
    fetch("/api/v1/rooms/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setRoom(jsonResponse));
  };

  useEffect(fetchRoom, []);

  // useEffect(() => {
  //   fetch("/api/v1/rooms/" + params.id)
  //     .then((response) => response.json())
  //     .then((jsonResponse) => setRoom(jsonResponse));
  // }, [params.id]);

  const editRoom = (e) => {
    e.preventDefault();
    setNameError(false);
    setBuildingError(false);
    if (room.name === "" || room.building === "") {
      if (room.name === "") {
        setNameError(true);
      }
      if (room.building === "") {
        setBuildingError(true);
      }
    } else {
      fetch("/api/v1/rooms/edit/" + params.id, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(room),
      }).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
          setChanged(false);
          fetchRoom();
        } else {
          setFailure(true);
          setSuccess(false);
          setNameError(true);
          setBuildingError(false);
        }
      });
    }
  };
  const updateProperty = (property, event) => {
    setRoom({
      ...room,
      [property]: event.target.value,
    });
    setChanged(true);
  };

  const handleDelete = () => {
    fetch(`/api/v1/rooms/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setRoom(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setBuildingError(false);
    setChanged(false);
  };

  const handleRestore = () => {
    fetch(`/api/v1/rooms/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setRoom(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setBuildingError(false);
    setChanged(false);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Redaguoti kabinetą</h2>
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atnaujintas
        </Alert>
      </Collapse>
      <Collapse in={failure}>
        <Alert
          onClose={() => {
            setFailure(false);
          }}
          severity="error"
          className="mb-3"
        >
          Įrašo nepavyko atnaujinti
        </Alert>
      </Collapse>
      <form noValidate>
        <table
          className="table table-hover shadow p-3 mb-5 bg-body rounded
        align-middle"
        >
          <tbody>
            <tr>
              <th scope="col">
                <label htmlFor="edit-room-name-with-error">
                  Pavadinimas *
                </label>
              </th>
              <td>
                <TextField
                  error={!!nameError}
                  onChange={(e) => updateProperty("name", e)}
                  value={room.name}
                  id="edit-room-name-with-error"
                  // label="Pavadinimas"
                  helperText="Pavadinimas negali būti tuščias"
                  className="form-control"
                  size="small"
                  InputLabelProps={{ shrink: true }}
                  required
                />
              </td>
            </tr>
            <tr>
              <th scope="col">
                <label htmlFor="edit-room-building-with-error">Pastatas *</label>
              </th>
              <td>
                <TextField
                  error={!!buildingError}
                  onChange={(e) => updateProperty("building", e)}
                  value={room.building}
                  id="create-room-building-with-error"
                  // label="Pastatas"
                  helperText="Pastatas negali būti tuščias"
                  className="form-control"
                  size="small"
                  InputLabelProps={{ shrink: true }}
                  required
                />
              </td>
            </tr>
            <tr>
            <th scope="col">
                <label htmlFor="edit-room-description">Aprašymas</label>
              </th>
              <td>
                <TextField
                  // error={!!buildingError}
                  onChange={(e) => updateProperty("description", e)}
                  value={room.description}
                  id="create-room-description"
                  // label="Aprašymas"
                  helperText="Neprivaloma"
                  className="form-control"
                  size="small"
                  InputLabelProps={{ shrink: true }}
                  // required
                />
              </td>
            </tr>
            <tr>
              <th scope="col">Būsena</th>
              <td>{room.deleted ? "Kabinetas ištrintas" : "Aktyvus"}</td>
            </tr>
            <tr>
              <th scope="col">Paskutinį kartą modifikuotas:</th>
              <td>{room.lastModifiedDate}</td>
            </tr>
          </tbody>
        </table>
        <button
          type="submit"
          className="btn btn-primary me-2"
          onClick={editRoom}
          disabled={!changed}
        >
          Redaguoti
        </button>

        {room.deleted ? (
          <button className="btn btn-secondary me-2" onClick={handleRestore}>
            Atstatyti
          </button>
        ) : (
          <button className="btn btn-danger me-2" onClick={handleDelete}>
            Ištrinti
          </button>
        )}
      </form>
    </div>
  );
}

export default EditRoomPage;
