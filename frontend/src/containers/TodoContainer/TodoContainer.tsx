// TodoContainer.tsx

import React, { useEffect, useState } from "react";
import AllTodos from "../../pages/AllTodos/AllTodos";
import { createTodo, getAllTodos } from "../../services/todo-services";
import { Todo } from "../../types/types";
import Modal from "../../components/Modal/Modal";
import "./TodoContainer.scss";

const TodoContainer: React.FC = () => {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [filter, setFilter] = useState<string>("all");
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [newTask, setNewTask] = useState<string>("");

  useEffect(() => {
    getAllTodos()
      .then((data) => setTodos(data))
      .catch((e) => console.error(e));
  }, []);

  const handleCreate = (title: string, description: string) => {
    const newTodo = { title, description };
    createTodo(newTodo)
      .then((createdTodo) => setTodos([...todos, createdTodo]))
      .catch((e) => console.error(e));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (newTask.trim()) {
      handleCreate(newTask, "");
      setNewTask("");
    }
  };

  return (
    <div className="todo-container">
      <div className="header">
        <h1>My Tasks</h1>
        <select onChange={(e) => setFilter(e.target.value)}>
          <option value="all">All</option>
          <option value="incomplete">Outlying</option>
          <option value="completed">Completed</option>
        </select>
      </div>
      <form className="add-task-input" onSubmit={handleSubmit}>
        <input 
          type="text" 
          placeholder="Add a task"
          value={newTask}
          onChange={(e) => setNewTask(e.target.value)} 
        />
        <button type="submit" className="add-btn">Add Task</button>
      </form>
      <AllTodos todos={todos} setTodos={setTodos} filter={filter} />
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSave={handleCreate}
        initialTitle={newTask}
        initialDescription=""
      />
    </div>
  );
};

export default TodoContainer;
