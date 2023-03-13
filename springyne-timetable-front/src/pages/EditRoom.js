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
          setTimeout(() => {
            setSuccess(false);
          }, 5000);
        } else {
          setFailure(true);
          setSuccess(false);
          setNameError(true);
          setBuildingError(false);
          setTimeout(() => {
            setFailure(false);
          }, 5000);
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
    setTimeout(() => {
      setSuccess(false);
    }, 5000);
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
    setTimeout(() => {
      setSuccess(false);
    }, 5000);
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
      <div className="container-fluid shadow p-3 mb-4 mb-md-5 bg-body rounded">
        <form noValidate>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-room-name-with-error">Pavadinimas *</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
              <TextField
                error={!!nameError}
                onChange={(e) => updateProperty("name", e)}
                value={room.name}
                id="edit-room-name-with-error"
                // label="Pavadinimas"
                helperText="Pavadinimas privalomas"
                className="form-control"
                size="small"
                InputLabelProps={{ shrink: true }}
                disabled={room.deleted}
                required
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-room-building-with-error">Pastatas *</label>
            </div>
            <div className="col-md-8 mb-2">
              <TextField
                error={!!buildingError}
                onChange={(e) => updateProperty("building", e)}
                value={room.building}
                id="edit-room-building-with-error"
                // label="Pastatas"
                helperText="Pastatas privalomas"
                className="form-control"
                size="small"
                InputLabelProps={{ shrink: true }}
                disabled={room.deleted}
                required
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-room-description">Aprašymas</label>
            </div>
            <div className="col-md-8 mb-2">
              <TextField
                // error={!!buildingError}
                onChange={(e) => updateProperty("description", e)}
                value={room.description}
                id="edit-room-description"
                // label="Aprašymas"
                helperText=""
                className="form-control"
                size="small"
                InputLabelProps={{ shrink: true }}
                disabled={room.deleted}
              // required
              />
            </div>
          </div>
        </form>
        <div className="row mb-md-4">
          <div className="col-md-4 mb-2 mb-md-0 fw-bold">Būsena</div>
          <div className="col-md-8 mb-2 mb-md-0">
            {room.deleted ? "Ištrintas" : "Aktyvus"}
          </div>
        </div>
        <div className="row mb-md-4">
          <div className="col-md-4 mb-2 mb-md-0 fw-bold">
            Paskutinį kartą modifikuotas
          </div>
          <div className="col-md-8 mb-2 mb-md-0">{room.lastModifiedDate}</div>
        </div>
      </div>
      {room.deleted ? (
        <div>
          <button
            type="submit"
            className="btn btn-primary me-2"
            onClick={editRoom}
            disabled
          >
            Redaguoti
          </button>


          <button className="btn btn-secondary me-2" 
          onClick={handleRestore}>
            Atstatyti
          </button>
          </div>
      ) : (
        <div>
          <button
            type="submit"
            className="btn btn-primary me-2"
            onClick={editRoom}>
            Redaguoti
          </button>
          <button 
          className="btn btn-danger me-2" 
          onClick={handleDelete}>
            Ištrinti
          </button>
        </div>
      )}
    </div>
  );
}

export default EditRoomPage;
