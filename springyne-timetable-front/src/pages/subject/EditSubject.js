import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import {
  TextField,
  FormControl,
  Select,
  MenuItem,
  InputLabel,
  OutlinedInput,
} from "@mui/material";
import ClearIcon from "@mui/icons-material/Clear";
import { apiUrl } from "../../App";

function EditSubjectPage() {
  const [subject, setSubject] = useState({});
  const [nameError, setNameError] = useState("");
  const [descriptionError, setDescriptionError] = useState(false);
  const [module, setModule] = useState({});
  const [room, setRoom] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [changed, setChanged] = useState(false);
  const params = useParams();
  const [rooms, setRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState("");
  const [selectedModule, setSelectedModule] = useState('');
  const [modules, setModules] = useState([]);
  const [moduleError, setModuleError] = useState(false);
  const [showModuleMenuItem, setShowModuleMenuItem] = useState(true);

  const fetchSubject = () => {
    fetch(`${apiUrl}/api/v1/subjects/` + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
  };

  useEffect(fetchSubject, [params.id]);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/rooms/`)
      .then(response => response.json())
      .then(setRooms)

  }, []);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/modules/`)
      .then(response => response.json())
      .then(setModules)

  }, []);

  const deleteRoom = (e) => {
    fetch(`${apiUrl}/api/v1/subjects/${params.id}/deleteRoom/${e}`, {
      method: "PATCH",
    }).then(fetchSubject)
  };
  const addRoom = (e) => {
    fetch(`${apiUrl}/api/v1/subjects/${params.id}/addRoom/${e}`, {
      method: "PATCH",
    }).then(fetchSubject);
  };

  const editsubject = (e) => {
    e.preventDefault();
    setNameError(false);
    if (subject.name === "" || subject.description === "") {
      if (subject.name === "") { setNameError(true); }
      if (subject.description === "") { setDescriptionError(true) }
    } else {
      fetch(
        `${apiUrl}/api/v1/subjects/edit/${params.id}?moduleId=${selectedModule}&roomId=${selectedRoom}`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(subject),
        }
      ).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
          setChanged(false);
          fetchSubject();
          setTimeout(() => {
            setSuccess(false);
          }, 5000);
        } else {
          setFailure(true);
          setSuccess(false);
          setTimeout(() => {
            setFailure(false);
          }, 5000);
        }
      });
    }
  };
  const updateProperty = (property, event) => {
    setSubject({
      ...subject,
      [property]: event.target.value,
    });
    // setChanged(true);
  };

  const handleDelete = () => {
    fetch(`${apiUrl}/api/v1/subjects/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setChanged(false);
    setTimeout(() => {
      setSuccess(false);
             }, 5000);
  };
  const handleRestore = () => {
    fetch(`${apiUrl}/api/v1/subjects/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setChanged(false);
    setTimeout(() => {
      setSuccess(false);
             }, 5000);
  };
  return (
    <div className="mx-3">
      <h2 className="my-5">Redaguoti dalyką</h2>

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
              <label htmlFor="subject-name">Pavadinimas *</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
              <TextField
                error={!!nameError}
                onChange={(e) => updateProperty("name", e)}
                value={subject.name}
                id="subject-name"
                label=""
                helperText="Pavadinimas privalomas"
                className="form-control mb-3"
                size="small"
                disabled={subject.deleted}
                InputLabelProps={{ shrink: true }}
                required
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="subject-description">Aprašymas *</label>
            </div>
            <div className="col-md-8 mb-2">
              <TextField
                error={!!descriptionError}
                onChange={(e) => updateProperty("description", e)}
                value={subject.description}
                multiline
                id="subject-description"
                label=""
                helperText="Aprašymas privalomas"
                className="form-control mb-3"
                size="small"
                disabled={subject.deleted}
                InputLabelProps={{ shrink: true }}
                required
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="add-module">Modulis</label>
            </div>
            <div className="col-md-8 mb-2">
              <FormControl fullWidth size="small" className="mb-3">
                <Select
                  labelId="add-module"
                  id="add-select-module"
                  fullWidth
                  value={selectedModule}
                  disabled={subject.deleted}
                  displayEmpty
                  onChange={(e) => setSelectedModule(e.target.value)}
                  onOpen={() => {
                    setShowModuleMenuItem(false);
                  }}
                  onClose={() => {
                    setShowModuleMenuItem(true);
                  }}
                >
                  <MenuItem
                    value=""
                    style={{ display: showModuleMenuItem ? "block" : "none" }}
                  >
                    {subject.module?.name}
                  </MenuItem>
                  {modules?.map((mod) => (
                    <MenuItem
                      key={mod.id}
                      value={mod.id}
                      disabled={mod.deleted}
                    >
                      {mod.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </div>
          </div>

          {/* ///////////// */}
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              {subject.rooms?.length === 0 ? "" : <div>Pašalinti kabinetą</div>}
            </div>
            <div className="col-md-8 mb-2">
              {subject.rooms?.map((room) => (
                <button
                  type="submit"
                  className="btn btn-light me-2 mb-2"
                  value={room.id}
                  // onChange={(e) => updateProperty(e.target.value)}
                  disabled={subject.deleted}
                  onClick={(e) => deleteRoom(e.target.value)}
                  key={room.id}
                  id={room.id}
                >
                  {room.name}{" "}
                  <ClearIcon
                    color="disabled"
                    sx={{ fontSize: 12 }}
                    value={room.id}
                    onClick={(e) => deleteRoom(room.id)}
                    key={`clearIcon-${room.id}`}
                    id={`clearIcon-${room.id}`}
                  />
                </button>
              ))}
            </div>
          </div>

          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="add-select-room">Pridėti kabinetą</label>
            </div>
            <div className="col-md-8 mb-2">
              <FormControl fullWidth size="small" className="mb-3">
                {/* <InputLabel id="select-room-label">Pridėti kabinetą</InputLabel> */}
                <Select
                  disabled={subject.deleted}
                  labelId="select-room-label"
                  InputLabelProps={{ shrink: true }}
                  id="add-select-room"
                  // label="Pridėti dalyką"
                  fullWidth
                  value={selectedRoom}
                  onChange={(e) => setSelectedRoom(e.target.value)}
                >
                  {rooms?.map((room) => (
                    <MenuItem
                      value={room.id}
                      key={room.id}
                      disabled={room.deleted}
                    >
                      {room.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </div>
          </div>

          <div className="row mb-md-4">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">Būsena</div>
            <div className="col-md-8 mb-2 mb-md-0">
              {subject.deleted ? "Ištrintas" : "Aktyvus"}
            </div>
          </div>
          <div className="row mb-md-4">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              Paskutinį kartą modifikuotas
            </div>
            <div className="col-md-8 mb-2 mb-md-0">{subject.last_Updated}</div>
          </div>
        </form>
      </div>
      <div>
        <button
          type="submit"
          className="btn btn-primary me-2 mb-5"
          onClick={editsubject}
          // disabled={!changed}
        >
          Redaguoti
        </button>
        {subject.deleted ? (
          <button
            className="btn btn-secondary me-2 mb-5"
            onClick={handleRestore}
          >
            Atstatyti
          </button>
        ) : (
          <button className="btn btn-danger me-2 mb-5" onClick={handleDelete}>
            Ištrinti
          </button>
        )}
      </div>
    </div>
  );
}

export default EditSubjectPage;
