import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";

function EditRoomPage() {
  const [room, setRoom] = useState({});
  const [nameError, setNameError] = useState("");
  const [buildingError, setBuildingError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetch("/api/v1/rooms/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setRoom(jsonResponse));
  }, [params.id]);

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
        } else {
          setFailure(true);
          setSuccess(false);
        }
      });
    }
  };
  const updateProperty = (property, event) => {
    setRoom({
      ...room,
      [property]: event.target.value,
    });
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
        <TextField
          error={!!nameError}
          onChange={(e) => updateProperty("name", e)}
          value={room.name}
          id="create-room-number-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          error={!!buildingError}
          onChange={(e) => updateProperty("building", e)}
          value={room.building}
          id="create-room-number-with-error"
          label="Pastatas"
          helperText="Pastatas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          // error={!!buildingError}
          onChange={(e) => updateProperty("description", e)}
          value={room.description}
          id="create-room-number-with-error"
          label="Aprašymas"
          helperText="Neprivaloma"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          // required
        />
        <button type="submit" className="btn btn-primary" onClick={editRoom}>
          Redaguoti
        </button>
      </form>
    </div>
  );
}

export default EditRoomPage;
