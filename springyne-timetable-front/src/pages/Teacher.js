import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";


export default function Teacher() {
  
  const [teachers, setTeachers] = useState([]);

  const { id } = useParams();

  useEffect(() => {
    loadTeachers();
  }, [id]);

  const loadTeachers = async () => {
    const result = await axios.get("http://localhost:8080/teachers");
    setTeachers(result.data);
  };

  const deleteTeacher = async (id) => {
    await axios.delete(`http://localhost:8080/teacher/${id}`);
    loadTeachers();
  };

  return (
    <div className="container">
      <div className="py-4">
        <table className="table border shadow">
          <thead>
            <tr>
              <th scope="col">Nr.</th>
              <th scope="col">Vardas</th>
              <th scope="col">Pavardė</th>
              <th scope="col">Email</th>
              <th scope="col">Veiksmai</th>
            </tr>
          </thead>
          <tbody>
            {teachers.map((teacher, index) => (
              <tr key={teacher.id}>
                <th scope="row">{index + 1}</th>
                <td>{teacher.name}</td>
                <td>{teacher.lastname}</td>
                <td>{teacher.email}</td>
                <td>
                  <Link
                    className="btn btn-primary mx-2"
                    to={`/Ziureti/${teacher.id}`}
                  >
                    Ziureti
                  </Link>
                  <Link
                    className="btn btn-outline-primary mx-2"
                    to={`/Redaguoti/${teacher.id}`}
                  >
                    Redaguoti
                  </Link>
                  <button
                    className="btn btn-danger mx-2"
                    onClick={() => deleteTeacher(teacher.id)}
                  >
                    Ištrinti
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}













