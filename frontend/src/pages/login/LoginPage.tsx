const LoginPage = () => {
  return (
    <div className="min-h-screen w-full flex items-center justify-center bg-gray-100 px-4">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md"
        style={{ boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.1)' }}>
        <h1 className="text-2xl font-bold text-center text-blue-900 mb-6">면접 REPO</h1>
        <form>
          <div className="mb-4">
            <input
              type="text"
              id="username"
              className="w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="아이디"
            />
          </div>
          <div className="mb-6">
            <input
              type="password"
              id="password"
              className="w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="비밀번호"
            />
          </div>
          <button
            type="submit"
            className="w-full bg-blue-900 text-white py-3 rounded-lg hover:bg-blue-800 transition"
          >
            로그인
          </button>
        </form>
        <div className="mt-4 text-center text-gray-500 text-sm">
          <a href="#" className="hover:underline text-gray-500">아이디 찾기</a> | <a href="#" className="hover:underline text-gray-500">비밀번호 찾기</a> | <a href="#" className="hover:underline text-gray-500">회원가입</a>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;