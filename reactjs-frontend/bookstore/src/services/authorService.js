import axios from 'axios';

const API_URL = 'http://localhost:8080/v1/authors';

const createAuthor = async (author) => {
  try {
    const response = await axios.post(API_URL, author, {
      headers: {
        'Content-Type': 'application/json',
      },
    });

    return response.data;
  } catch (error) {
    console.error('Error creating author:', error.message);
    throw error;
  }
};

export default {
  createAuthor,
};