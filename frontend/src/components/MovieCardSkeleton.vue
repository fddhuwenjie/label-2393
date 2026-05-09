<template>
  <div class="movie-card-skeleton">
    <div class="poster-skeleton">
      <div class="shimmer"></div>
    </div>
    <div class="info-skeleton">
      <div class="title-skeleton shimmer-line"></div>
      <div class="meta-skeleton">
        <div class="tag-skeleton shimmer-line"></div>
        <div class="tag-skeleton shimmer-line"></div>
      </div>
      <div class="director-skeleton shimmer-line"></div>
      <div class="count-skeleton shimmer-line"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MovieCardSkeleton'
}
</script>

<style scoped>
.movie-card-skeleton {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.poster-skeleton {
  position: relative;
  padding-top: 140%;
  background: linear-gradient(135deg, #f0f0f0 0%, #e8e8e8 100%);
  overflow: hidden;
}

.shimmer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent 0%, rgba(255,255,255,0.4) 50%, transparent 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.info-skeleton {
  padding: 18px;
}

.shimmer-line {
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
  border-radius: 8px;
}

.title-skeleton {
  height: 18px;
  width: 75%;
  margin-bottom: 12px;
}

.meta-skeleton {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.tag-skeleton {
  height: 22px;
  width: 50px;
  border-radius: 12px;
}

.director-skeleton {
  height: 14px;
  width: 55%;
  margin-bottom: 10px;
}

.count-skeleton {
  height: 12px;
  width: 35%;
}

@keyframes shimmer {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}
</style>
