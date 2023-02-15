import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";



function EditSubjectPage() {
  const [subject, setSubject] = useState({});
  const [nameError, setNameError] = useState("");
  const [module, setModule] = useState({});
  const [room, setRoom] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const params = useParams();
  const [rooms, setRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState("");
  const [selectedModule, setSelectedModule] = useState('');
  const [modules, setModules] = useState([]);

  useEffect(() => {
    fetch("/api/v1/subjects/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
  }, [params.id]);

  useEffect(() => {
    fetch('api/v1/rooms/')
      .then(response => response.json())
      .then(setRooms)

  }, []);
  
  useEffect(() => {
    fetch('api/v1/modules/')
      .then(response => response.json())
      .then(setModules)

  }, []);
  
  const deleteRoom  = (e) => {
    fetch(`/api/v1/subjects/${params.id}/deleteRoom/${e}`, {
      method: "PATCH",
    }).then(window.location.reload(true))
  };

  const editsubject = (e) => {
    e.preventDefault();
    setNameError(false);
    if (subject.name == "") {
      setNameError(true);
    } else {
      fetch(`/api/v1/subjects/edit/${params.id}?
      roomId=${selectedRoom}&moduleId=${selectedModule}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(subject),
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
    setSubject({
      ...subject,
      [property]: event.target.value,
    });
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
      <form noValidate>
        <TextField
          error={!!nameError}
          onChange={(e) => updateProperty("name", e)}
          value={subject.name}
          id="create-subject-number-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          onChange={(e) => updateProperty("description", e)}
          value={subject.description}
          id="create-subject-number-with-error"
          label="Aprašymas"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}

        />
        <label htmlFor="page-size-select" className="mb-3">
        Modulis:
      </label>
      <select
            value={selectedModule}
            onChange={(e) => setSelectedModule(e.target.value)}
            className="form-control mb-3">
              <option value=''>{subject.module?.name}</option>
            {
                modules.map(
                    (mod) =>
                    (<option key={mod.id} 
                        value={mod.id}>{mod.name}</option>)
                )
            }
        </select>

      
        <label htmlFor="page-size-select" className="mb-3">
          Kabinetai:
        </label>
        <div className="d-grid gap-6 d-md-block">
          {subject.rooms?.map((room) => (
          <button
            type="submit"
            className="btn btn-light"
            value={room.id}
            onClick={(e) => deleteRoom(e.target.value)}
            key={room.id} id={room.id}>{room.name}</button>
        ))}</div>

        <select
          value={selectedRoom}
          onChange={(e) => setSelectedRoom(e.target.value)}
          className="form-control mb-3">
          <option value=''>---</option>
          {
            rooms.map(
              (room) =>
              (<option key={room.id} 
                  value={room.id}>{room.name}</option>)
          )
          }
        </select>
        <button type="submit" className="btn btn-primary" onClick={editsubject}>
          Redaguoti
        </button>
      </form>
    </div>
  );
}

export default EditSubjectPage;
